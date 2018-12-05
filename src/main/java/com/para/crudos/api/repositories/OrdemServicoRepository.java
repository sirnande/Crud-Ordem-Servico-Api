package com.para.crudos.api.repositories;

import java.util.List;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.para.crudos.api.model.OrdemServico;

@Repository
@NamedQueries({
	@NamedQuery(name = "OrdemServicorepository.findByClienteId",
			query = "SELECT os "
				  + "FROM OrdemServico os "
				  + "WHERE os.cliente.id = :clienteId")
})
public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long>{

	@Transactional(readOnly = true)
	List<OrdemServico> findByClienteId(@Param("clienteId") Long clienteId);
	
	
	@Transactional(readOnly = true)
	Page<OrdemServico> findByClienteId(@Param("clienteId") Long clienteId, Pageable pageable);
	
}
