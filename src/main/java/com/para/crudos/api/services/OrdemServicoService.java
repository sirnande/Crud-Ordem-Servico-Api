package com.para.crudos.api.services;

import com.para.crudos.api.auditoria.Auditoria;
import com.para.crudos.api.dtos.OrdemServicoDTO;
import com.para.crudos.api.exceptions.ValidacaoException;
import com.para.crudos.api.model.Endereco;
import com.para.crudos.api.model.OrdemServico;
import com.para.crudos.api.repositories.OrdemServicoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Service
public class OrdemServicoService {

    private static final Logger log = LoggerFactory.getLogger(OrdemServicoService.class);

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private TecnicoService tecnicoService;

    @Value("${paginacao.qtd_por_pagina}")
    private int qtdPorPagina;


    @Autowired
    private ConversionService conversionService;

    private Auditoria<OrdemServicoDTO> auditoria = new Auditoria<>();


    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    public OrdemServicoDTO gravar(OrdemServicoDTO ordemServicoDTO) {
        log.info("Gravando uma nova OrdemServicoService: {}", ordemServicoDTO.toString());

        validarCliente(ordemServicoDTO);
        validarEndereco(ordemServicoDTO);
        validarTecnico(ordemServicoDTO);
        OrdemServico ordemServico = this.conversionService.convert(ordemServicoDTO, OrdemServico.class);

        ordemServico = this.ordemServicoRepository.save(ordemServico);

        ordemServicoDTO = this.conversionService.convert(ordemServico, OrdemServicoDTO.class);

        return ordemServicoDTO;
    }

      public Page<OrdemServicoDTO> buscarPorClienteId(Long idCliente, int pag, int qtdItens, String ord, String dir) {
        log.info("Buscando ordem de sevico por id: {}", idCliente);
        this.clienteService.buscarPorId(idCliente);

        PageRequest pageRequest =   PageRequest.of(pag, qtdItens, Sort.Direction.valueOf(dir), ord);
        Page<OrdemServico> ordemServicos = this.ordemServicoRepository.findByClienteId(idCliente, pageRequest);
        Page<OrdemServicoDTO> ordemServicoDTOS = null;
        if(!ordemServicos.isEmpty())
            ordemServicoDTOS = ordemServicos.map(ordemServico -> this.conversionService.convert(ordemServico, OrdemServicoDTO.class));

        return ordemServicoDTOS;
    }

    public List<OrdemServico> bucarPorClienteId(Long clienteId){
        this.clienteService.buscarPorId(clienteId);
        return this.ordemServicoRepository.findByClienteId(clienteId);
    }




    public OrdemServicoDTO buscarPorId(Long id) {
        log.info("Buscar uma Ordem de Servico pelo id: {}", id);

        OrdemServico ordemServico = this.ordemServicoRepository.findById(id)
                .orElseThrow(() -> new ValidacaoException("Ordem de Servico não encontrada para o ID: "+ id));

        return this.conversionService.convert(ordemServico, OrdemServicoDTO.class);
    }


    public OrdemServicoDTO atualizar(OrdemServicoDTO ordemServicoDTO) {
        log.info("Atualizando OrdemServicoService: {}", ordemServicoDTO.toString());

        buscarPorId(ordemServicoDTO.getId());

        OrdemServico ordemServico =  this.conversionService.convert(buscarPorId(ordemServicoDTO.getId()), OrdemServico.class);

        validarCliente(ordemServicoDTO);
        validarEndereco(ordemServicoDTO);
        validarTecnico(ordemServicoDTO);

        ordemServico = this.conversionService.convert(ordemServicoDTO, OrdemServico.class);
        this.ordemServicoRepository.save(ordemServico);

        return this.conversionService.convert(ordemServico, OrdemServicoDTO.class);
    }



    public String remover(Long id) {
        log.info("Removendo ordem de Servico id: {}", id);
        buscarPorId(id);
        this.ordemServicoRepository.deleteById(id);

        return "Ordem de servico excluido com sucesso...";
    }



    private void validarTecnico(OrdemServicoDTO ordemServicoDTO) {
        log.info("Validando Tecnico");
        if(ordemServicoDTO.getTecnico() == null)
            throw new ValidacaoException("Tecnico não informado.");

        if(this.tecnicoService.buscarPorId(ordemServicoDTO.getEndereco()) == null){
            throw new ValidacaoException("Tecnico não encontrado");
        }
    }


    private void validarEndereco(OrdemServicoDTO ordemServicoDTO) {
        log.info("Validando Endereco");
        if(ordemServicoDTO.getEndereco() == null)
            throw new ValidacaoException("endereco não informado");


        if (this.enderecoService.buscarPorId(ordemServicoDTO.getEndereco()) == null)
            throw  new ValidacaoException("Endereco não encontrado");

    }


    private void validarCliente(OrdemServicoDTO ordemServicoDTO) {
        log.info("Validando Cliente");
        if(ordemServicoDTO.getCliente() == null)
            throw  new ValidacaoException("Cliente não informado");


        if(this.clienteService.buscarPorId(ordemServicoDTO.getCliente()) == null)
            throw new ValidacaoException("Cliente não encontrado. ID inexixtente.");

    }
}
