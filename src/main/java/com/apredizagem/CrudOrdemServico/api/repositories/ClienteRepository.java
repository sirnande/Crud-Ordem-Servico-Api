package com.apredizagem.CrudOrdemServico.api.repositories;

import com.apredizagem.CrudOrdemServico.api.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{

    @Transactional(readOnly = true)
    Cliente findByNome(String nome);

    @Transactional(readOnly = true)
    Cliente findByEmail(String email);

    @Transactional(readOnly = true)
    Cliente findByTelefone(String telefone);

}
