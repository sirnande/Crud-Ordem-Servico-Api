package com.apredizagem.CrudOrdemServico.api.repositories;

import com.apredizagem.CrudOrdemServico.api.model.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {

}
