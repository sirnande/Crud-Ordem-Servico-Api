package com.apredizagem.CrudOrdemServico.api.repositories;

import com.apredizagem.CrudOrdemServico.api.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    @Transactional(readOnly = true)
    List<Endereco> findByRua(String rua);


    @Transactional(readOnly = true)
    List<Endereco> findByCidade(String cidade);
}
