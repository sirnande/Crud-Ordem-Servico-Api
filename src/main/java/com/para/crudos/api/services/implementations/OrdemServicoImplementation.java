package com.para.crudos.api.services.implementations;

import com.para.crudos.api.model.OrdemServico;
import com.para.crudos.api.repositories.OrdemServicoRepository;
import com.para.crudos.api.services.OrdemServicoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrdemServicoImplementation implements OrdemServicoService {

    private static final Logger log = LoggerFactory.getLogger(OrdemServicoImplementation.class);

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @Override
    public OrdemServico persistir(OrdemServico ordemServico) {
        log.info("Persistir OrdemServico: {}", ordemServico);
        return this.ordemServicoRepository.save(ordemServico);
    }

    @Override
    public Page<OrdemServico> buscarPorClienteId(Long id, PageRequest pageRequest) {
        log.info("Buscando ordem de sevico por id: {}", id);
        return this.ordemServicoRepository.findByClienteId(id, pageRequest);
    }



    @Override
    public void remover(Long id) {
        log.info("Removendo ordem de Servico id: {}", id);
        this.ordemServicoRepository.deleteById(id);
    }


    @Override
    public Optional<OrdemServico> buscarPorId(Long id) {
        log.info("Buscar uma Ordem de Servico pelo id: {}", id);
        return this.ordemServicoRepository.findById(id);
    }
}
