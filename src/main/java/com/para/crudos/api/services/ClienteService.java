package com.para.crudos.api.services;

import com.para.crudos.api.dtos.ClienteDTO;
import com.para.crudos.api.exceptions.ValidacaoException;
import com.para.crudos.api.model.Cliente;
import com.para.crudos.api.repositories.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Optional;

@Service
public class ClienteService implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(ClienteService.class);

    @Autowired
    private ClienteRepository clienteRepository;


     public Cliente atualizar(Cliente cliente){
        log.info("Atualizando cliente: {}", cliente.toString());
        return Optional.ofNullable(this.clienteRepository.save(cliente))
                .orElseThrow(() -> new ValidacaoException("Erro ao atualizar cliente  "+ cliente.toString()));
    }

    public Cliente gravar(Cliente cliente){
        log.info("Criando um novo cliente: {}", cliente.toString());
        return Optional.ofNullable(this.clienteRepository.save(cliente))
                .orElseThrow(() -> new ValidacaoException("Erro ao criar um novo cliente"+ cliente.toString()));
    }

    public Cliente buscarPorId(Long id) {
        log.info("Buscar um cliente pelo id: {}", id);
        return this.clienteRepository.findById(id)
                .orElseThrow(() -> new ValidacaoException("Cliente não encontrado para o ID: "+id));
    }

    public Cliente buscarPorCpf(String cpf) {
        log.info("Buscando por cliente pelo cpf: }", cpf );
        return Optional.ofNullable(this.clienteRepository.findByCpf(cpf))
                .orElseThrow(() -> new ValidacaoException("Cliente não encontrado para o CPF: " + cpf));
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

    public void remover(Long id) {
        log.info("Removendo um cliente da base de dados com  id: []", id);
        this.clienteRepository.deleteById(id);
    }


    public void validarDadosExistentes(ClienteDTO clienteDto) {


        if(this.buscarPorCpf(clienteDto.getCpf()) != null)
            throw new ValidacaoException("CPF já existe");


        if (this.buscarPorEmail(clienteDto.getEmail()) != null)
            throw new ValidacaoException("EMAIL já existente");

    }


}
