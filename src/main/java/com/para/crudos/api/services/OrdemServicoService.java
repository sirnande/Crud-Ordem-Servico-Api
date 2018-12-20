package com.para.crudos.api.services;

import com.para.crudos.api.repositories.OrdemServicoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrdemServicoService {

    private static final Logger log = LoggerFactory.getLogger(OrdemServicoService.class);

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    public com.para.crudos.api.model.OrdemServico persistir(com.para.crudos.api.model.OrdemServico ordemServico) {
        log.info("Persistir OrdemServicoService: {}", ordemServico);
        return this.ordemServicoRepository.save(ordemServico);
    }

    public Page<com.para.crudos.api.model.OrdemServico> buscarPorClienteId(Long id, PageRequest pageRequest) {
        log.info("Buscando ordem de sevico por id: {}", id);
        return this.ordemServicoRepository.findByClienteId(id, pageRequest);
    }



    public void remover(Long id) {
        log.info("Removendo ordem de Servico id: {}", id);
        this.ordemServicoRepository.deleteById(id);
    }


    public Optional<com.para.crudos.api.model.OrdemServico> buscarPorId(Long id) {
        log.info("Buscar uma Ordem de Servico pelo id: {}", id);
        return this.ordemServicoRepository.findById(id);
    }
}
