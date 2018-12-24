package com.para.crudos.api.services;

import com.para.crudos.api.auditoria.Auditoria;
import com.para.crudos.api.dtos.EnderecoDTO;
import com.para.crudos.api.exceptions.ValidacaoException;
import com.para.crudos.api.model.Cliente;
import com.para.crudos.api.model.Endereco;
import com.para.crudos.api.repositories.EnderecoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class EnderecoService {

    private static final Logger log = LoggerFactory.getLogger(EnderecoService.class);

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ConversionService conversionService;

    private Auditoria<EnderecoDTO> auditoria = new Auditoria<>();

    public EnderecoDTO salvar(EnderecoDTO enderecoDto){
        log.info("Salvar um nome endereco: {} ", enderecoDto.toString());
        validarDadosExistentes(enderecoDto);
        Endereco endereco = Optional.ofNullable(this.enderecoRepository.save(this.conversionService.convert(enderecoDto, Endereco.class)))
                .orElseThrow(() -> new ValidacaoException("Erro ao salvar o endereco " + enderecoDto.toString()));

        return this.conversionService.convert(endereco, EnderecoDTO.class);
    }

    public EnderecoDTO atualizar(EnderecoDTO enderecoDto){
        log.info("Atualizar o endereco: {}"+ enderecoDto.toString());

        Endereco endereco = this.buscarPorId(enderecoDto.getId());

        //auditoria.post(this.conversionService.convert(endereco, EnderecoDTO.class), enderecoDto, "Endereco");
        enderecoDto = this.conversionService.convert(
                this.enderecoRepository.save(this.conversionService.convert(enderecoDto, Endereco.class)),

                EnderecoDTO.class
        );

        return enderecoDto;
    }

    public EnderecoDTO buscarPorCep(String cep) {
        log.info("Buscar endereco por cep: {}", cep);
        Endereco endereco = Optional.ofNullable(this.enderecoRepository.findByCep(cep))
                .orElseThrow(() -> new ValidacaoException("Endereco não encontrado para o CEP: "+ cep));
        return this.conversionService.convert(endereco, EnderecoDTO.class);
    }


    public List<EnderecoDTO> buscarPorRua(String rua) {
        log.info("Buscar endereco por rua: {}", rua);
        List<Endereco> enderecos = this.enderecoRepository.findByRua(rua);
        if (enderecos.isEmpty()){
            throw new ValidacaoException("Endereço não encontrado para a rua: "+ rua);
        }
        return enderecos.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    public List<EnderecoDTO> buscarPorCidade(String cidade) {
        log.info("Buscar endereco por cidade: {}", cidade);
        List<Endereco> enderecos = this.enderecoRepository.findByCidade(cidade);
        if (enderecos.isEmpty()){
            throw new ValidacaoException("Endereço não encontrado para a cidade: "+ cidade);
        }
        return enderecos.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }



    public Endereco buscarPorId(Long id) {
        return this.enderecoRepository.findById(id)
                .orElseThrow(() -> new ValidacaoException("Endereço não encontrado para o id: "+ id));
    }

    public String remover(Long id) {
        log.info("Removendo um endereco por id: {}", id);

        Endereco endereco = buscarPorId(id);
  //      auditoria.post(this.conversionService.convert(endereco, EnderecoDTO.class), new EnderecoDTO(), "Endereco");
        this.enderecoRepository.deleteById(id);
        return "Endereco deletado com sucesso......";
    }


    public void validarDadosExistentes(EnderecoDTO enderecoDto) {

        if(this.enderecoRepository.findByCep(enderecoDto.getCep()) != null) {
            throw new ValidacaoException("endereco CEP já existente.");
        }
    }

    private EnderecoDTO toDto(Endereco endereco) {
        return conversionService.convert(endereco, EnderecoDTO.class);
    }
}
