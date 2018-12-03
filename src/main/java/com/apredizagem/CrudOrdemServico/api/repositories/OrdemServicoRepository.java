package com.apredizagem.CrudOrdemServico.api.repositories;

import com.apredizagem.CrudOrdemServico.api.model.OrdemServico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.List;


@Repository
@NamedQueries({
        @NamedQuery(name = "OrdemServicoRepository.findByClienteCpf",
        query = "SELECT os " +
                "FROM OrdemServico os " +
                "WHERE os.cliente.cpf = :clienteCpf")
})
public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {

    @Transactional(readOnly = true)
    List<OrdemServico> findByClienteCpf(@Param("clienteCpf") Long clienteCpf);


    @Transactional(readOnly = true)
    Page<OrdemServico> findByClienteCpf(@Param("clienteCpf") Long clienteCpf, Pageable pageable);
}
