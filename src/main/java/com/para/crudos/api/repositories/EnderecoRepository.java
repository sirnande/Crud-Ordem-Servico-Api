package com.para.crudos.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.para.crudos.api.model.Endereco;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long>{

	@Transactional(readOnly = true)
	List<Endereco> findByRua(String rua);
	
	@Transactional(readOnly = true)
	List<Endereco> findByCidade(String cidade);

	@Transactional(readOnly = true)
	Endereco findByCep(String cep);

//	@Transactional(readOnly = true)
//	Endereco findOne(Long id);
}
