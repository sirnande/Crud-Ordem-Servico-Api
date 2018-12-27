package com.para.crudos.api.services;

import com.para.crudos.api.auditoria.Auditoria;
import com.para.crudos.api.dtos.ClienteDTO;
import com.para.crudos.api.exceptions.ValidacaoException;
import com.para.crudos.api.model.Cliente;
import com.para.crudos.api.repositories.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Optional;

@Service
public class ClienteService implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(ClienteService.class);

    @Autowired
    private ClienteRepository clienteRepository;


    @Autowired
    private ConversionService conversionService;


    Auditoria<ClienteDTO>  auditoria = new Auditoria<>();


    public ClienteDTO gravar(ClienteDTO clienteDto){
        log.info("Criando um novo cliente: {}", clienteDto.toString());

        validarDadosExistentes(clienteDto);

        //auditoria.post(new ClienteDTO(), clienteDto, "Cliente");

        Cliente cliente = Optional.ofNullable(this.clienteRepository.save(this.conversionService.convert(clienteDto, Cliente.class)))
                .orElseThrow(() -> new ValidacaoException("Erro ao criar um novo cliente"+ clienteDto.toString()));

        return this.conversionService.convert(cliente, ClienteDTO.class);
    }

    public ClienteDTO atualizar(ClienteDTO clienteDto){
        log.info("Atualizando cliente: {}", clienteDto.toString());
        Cliente clienteAnt = buscarPorId(clienteDto.getId());
        Cliente cliente = Optional.ofNullable(this.clienteRepository.save(this.conversionService.convert(clienteDto, Cliente.class)))
                .orElseThrow(() -> new ValidacaoException("Erro ao atualizar cliente  "+ clienteDto.toString()));

        return this.conversionService.convert(
                   cliente,

                    ClienteDTO.class
                );
    }



    public Cliente buscarPorId(Long id) {
        log.info("Buscar um cliente pelo id: {}", id);
        return this.clienteRepository.findById(id)
                .orElseThrow(() -> new ValidacaoException("Cliente não encontrado para o ID: "+id));
    }

    public ClienteDTO buscarPorCpf(String cpf) {
        log.info("Buscando por cliente pelo cpf: }", cpf );

        return this.conversionService.convert(
                Optional.ofNullable(this.clienteRepository.findByCpf(cpf))
                        .orElseThrow(() -> new ValidacaoException("Cliente não encontrado para o CPF: " + cpf)),

                ClienteDTO.class
        );
    }

    public Cliente buscarPorNome(String nome) {
        log.info("Busca cliente pelo nome: {}", nome);
        return Optional.ofNullable(this.clienteRepository.findByNome(nome))
                .orElseThrow(() -> new ValidacaoException("Cliente não encontrado para o NOME: "+ nome));
    }

    public Cliente buscarPorTelefone(String telefone) {
        log.info("Buscar cliente pelo telefone: {}", telefone);
        return Optional.ofNullable(this.clienteRepository.findByTelefone(telefone))
                .orElseThrow(() -> new ValidacaoException("Cliente não encontrado para o TELEFONE: "+ telefone));
    }

    public Cliente buscarPorEmail(String email) {
        log.info("bucar cliente pelo email: {}", email);
        return Optional.ofNullable(this.clienteRepository.findByEmail(email))
                .orElseThrow(() -> new ValidacaoException("Cliente não encontrado para o EMAIL: "+ email));
    }

    public String remover(Long id) {
        log.info("Removendo um cliente da base de dados com  id: []", id);

        Cliente cliente = buscarPorId(id);
       // auditoria.post(this.conversionService.convert(cliente, ClienteDTO.class), new ClienteDTO(), "Cliente");
        this.clienteRepository.deleteById(id);

        return "Cliente excluido com sucesso....";
    }


    public void validarDadosExistentes(ClienteDTO clienteDto) {


        if(this.clienteRepository.findByCpf(clienteDto.getCpf()) != null)
            throw new ValidacaoException("CPF já existe");


        if (this.clienteRepository.findByEmail(clienteDto.getEmail()) != null)
            throw new ValidacaoException("EMAIL já existente");

    }


}
