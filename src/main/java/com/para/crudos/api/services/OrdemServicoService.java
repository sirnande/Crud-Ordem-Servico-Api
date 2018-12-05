package com.para.crudos.api.services;

import com.para.crudos.api.model.OrdemServico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface OrdemServicoService {

    /**
     * Persistrir uma nova Ordem de Servico na base de dados
     *
     * @param ordemServico
     * @return OrdemServico
     */
    OrdemServico persistir(OrdemServico ordemServico);


    /**
     * Buscar e retorna uma lista paginada de um determinado cliente
     *
     * @param id
     * @param pageRequest
     * @return Page<OrdemServico>
     */

    Page<OrdemServico>  buscarPorClienteId(Long id, PageRequest pageRequest);


    /**
     * Buscar Ordem de servico por ID
     *
     * @param id
     * @return Optonal<OrdemServico>
     */
    Optional<OrdemServico> buscarPorId(Long id);


    /**
     * Remove uma OrdemServico da basa de dados
     *
     * @param id
     */
    void remover(Long id);

}
