package com.para.crudos.api.repositories;

import com.para.crudos.api.model.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {

	@Transactional(readOnly = true)
	Tecnico findByNome(String nome);

}
