package com.para.crudos.api.services;

import com.para.crudos.api.model.Endereco;
import com.para.crudos.api.repositories.EnderecoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {

    private static final Logger log = LoggerFactory.getLogger(EnderecoService.class);

    @Autowired
    private EnderecoRepository enderecoRepository;

    public Endereco persistir(Endereco endereco) {
        log.info("Persistindo um endereco: {}", endereco);
        return this.enderecoRepository.save(endereco);
    }

    public Optional<Endereco> buscarPorCep(String cep) {
        log.info("Buscar endereco por cep: {}", cep);
        return Optional.ofNullable(this.enderecoRepository.findByCep(cep));
    }


    public Optional<List<Endereco>> buscarPorRua(String rua) {
        log.info("Buscar endereco por rua: {}", rua);
        return Optional.ofNullable(this.enderecoRepository.findByRua(rua));
    }

    public Optional<List<Endereco>> buscarPorCidade(String cidade) {
        return Optional.of(this.enderecoRepository.findByCidade(cidade));
    }

    public Optional<Endereco> buscarPorId(Long id) {
        return this.enderecoRepository.findById(id);
    }

    public void remover(Long id) {
        log.info("Removendo um endereco por id: {}", id);
        this.enderecoRepository.deleteById(id);
    }
}
