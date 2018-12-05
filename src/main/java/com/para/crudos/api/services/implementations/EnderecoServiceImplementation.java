package com.para.crudos.api.services.implementations;

import com.para.crudos.api.model.Endereco;
import com.para.crudos.api.repositories.EnderecoRepository;
import com.para.crudos.api.services.EnderecoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoServiceImplementation implements EnderecoService {

    private static final Logger log = LoggerFactory.getLogger(EnderecoServiceImplementation.class);

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Override
    public Endereco persistir(Endereco endereco) {
        log.info("Persistindo um endereco: {}", endereco);
        return this.enderecoRepository.save(endereco);
    }

    @Override
    public Optional<Endereco> buscarPorCep(String cep) {
        log.info("Buscar endereco por cep: {}", cep);
        return Optional.ofNullable(this.enderecoRepository.findByCep(cep));
    }

    @Override
    public Optional<List<Endereco>> buscarPorRua(String rua) {
        log.info("Buscar endereco por rua: {}", rua);
        return Optional.ofNullable(this.enderecoRepository.findByRua(rua));
    }

    @Override
    public Optional<List<Endereco>> buscarPorCidade(String cidade) {
        return Optional.of(this.enderecoRepository.findByCidade(cidade));
    }

    @Override
    public Optional<Endereco> buscarPorId(Long id) {
        return this.enderecoRepository.findById(id);
    }
}
