package com.para.crudos.api.services;

import com.para.crudos.api.model.Tecnico;
import com.para.crudos.api.repositories.TecnicoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TecnicoService{

    private static  final Logger log = LoggerFactory.getLogger(TecnicoService.class);

    @Autowired
    private TecnicoRepository tecnicoRepository;



    public Tecnico persistir(Tecnico tecnico) {
        log.info("Persistir um tecnico: {}", tecnico);
        return this.tecnicoRepository.save(tecnico);
    }


    public Optional<Tecnico> buscarPorId(Long id) {
        return this.tecnicoRepository.findById(id);
    }

    public Optional<Tecnico> buscarPorNome(String nome) {
        log.info("Buscar tecnico por nome: {}", nome);
        return Optional.ofNullable(this.tecnicoRepository.findByNome(nome));
    }



    public void remover(Long id) {
        log.info("Deletando um novo tecnico por id: {}", id);
        this.tecnicoRepository.deleteById(id);
    }
}
