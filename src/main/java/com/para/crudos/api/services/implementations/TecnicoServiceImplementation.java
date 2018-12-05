package com.para.crudos.api.services.implementations;

import com.para.crudos.api.model.Tecnico;
import com.para.crudos.api.repositories.TecnicoRepository;
import com.para.crudos.api.services.TecnicoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TecnicoServiceImplementation implements TecnicoService {

    private static  final Logger log = LoggerFactory.getLogger(TecnicoServiceImplementation.class);

    @Autowired
    private TecnicoRepository tecnicoRepository;


    @Override
    public Tecnico persistir(Tecnico tecnico) {
        log.info("Persistir um tecnico: {}", tecnico);
        return this.tecnicoRepository.save(tecnico);
    }

    @Override
    public Optional<Tecnico> buscarPorNome(String nome) {
        log.info("Buscar tecnico por nome: {}", nome);
        return Optional.ofNullable(this.tecnicoRepository.findByNome(nome));
    }


}
