package com.para.crudos.api.controllers;

import com.para.crudos.api.auditoria.Auditoria;
import com.para.crudos.api.dtos.OrdemServicoDTO;
import com.para.crudos.api.enums.Status;
import com.para.crudos.api.model.Cliente;
import com.para.crudos.api.model.Endereco;
import com.para.crudos.api.model.OrdemServico;
import com.para.crudos.api.model.Tecnico;
import com.para.crudos.api.response.Response;
import com.para.crudos.api.services.ClienteService;
import com.para.crudos.api.services.EnderecoService;
import com.para.crudos.api.services.OrdemServicoService;
import com.para.crudos.api.services.TecnicoService;
import com.para.crudos.api.setup.UrlApi;
import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

@RestController
@RequestMapping(UrlApi.URL + "/os")
public class OrdemServicoController {
    private static final Logger log = LoggerFactory.getLogger(OrdemServicoController.class);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private OrdemServicoService ordemServicoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private TecnicoService tecnicoService;

    @Value("${paginacao.qtd_por_pagina}")
    private int qtdPorPagina;


    private Auditoria<OrdemServicoDTO> auditoria = new Auditoria<>();


    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<Response<Page<OrdemServicoDTO>>> listarPorClienteId(@PathVariable("clienteId") long clienteId,
                                                                              @RequestParam(value = "pag", defaultValue = "0") int pag,
                                                                              @RequestParam(value = "ord", defaultValue = "id") String ord,
                                                                              @RequestParam(value = "dir", defaultValue = "DESC") String dir){

        log.info("Buscando lançamento por ID de cliente: {}, página: {}", clienteId, pag);
        Response<Page<OrdemServicoDTO>>response = new Response<>();

        PageRequest pageRequest =   PageRequest.of(pag, this.qtdPorPagina, Sort.Direction.valueOf(dir), ord);
        Page<OrdemServico> ordemServicos = this.ordemServicoService.buscarPorClienteId(clienteId, pageRequest);
        if(ordemServicos == null){
            response.getErros().add("Erro cliente não encontrado para o id: "+ clienteId);
            return ResponseEntity.badRequest().body(response);
        }

        Page<OrdemServicoDTO> ordemServicoDtos = ordemServicos.map(ordemServico -> this.converterOrdemServicoDto(ordemServico));


        response.setData(ordemServicoDtos);
        return ResponseEntity.ok(response);

    }



    @GetMapping("/{id}")
    public ResponseEntity<Response<OrdemServicoDTO>> listarporId(@PathVariable("id") Long id){
        log.info("Buscar orddem de servico por id: {}", id);

        Response<OrdemServicoDTO> response =  new Response<>();
        Optional<OrdemServico> ordemServico = this.ordemServicoService.buscarPorId(id);

        if(!ordemServico.isPresent()){
            log.info("Ordem de servico não encontardo pelo ID: {}", id);
            response.getErros().add("Ordem de servico não encontardo pelo ID: " + id);
            return ResponseEntity.badRequest().body(response);
        }

        response.setData(this.converterOrdemServicoDto(ordemServico.get()));
        return  ResponseEntity.ok(response);
    }



    @PostMapping("/cadastro-os")
    public ResponseEntity<Response<OrdemServicoDTO>> adicionar(@Valid @RequestBody OrdemServicoDTO ordemServicoDto,
                                                               BindingResult result) throws ParseException {
        log.info("Adicionar uma Ordem de Serviço: {}", ordemServicoDto.toString());

        Response<OrdemServicoDTO> response = new Response<>();
        validarCliente(ordemServicoDto, result);
        validarEndereco(ordemServicoDto, result);
        validarTecnico(ordemServicoDto, result);
        OrdemServico ordemServico = this.converterDtoParaOrdemServico(ordemServicoDto, result);

        if(result.hasErrors()){
            log.error("Erro validando Ordem Serviço: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }
        this.auditoria.post(new OrdemServicoDTO(), ordemServicoDto, "OrdemServicoService");
        this.ordemServicoService.persistir(ordemServico);
        response.setData(this.converterOrdemServicoDto(ordemServico));
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Response<OrdemServicoDTO>> atualizar(@PathVariable("id") Long id,
                                                               @Valid @RequestBody OrdemServicoDTO ordemServicoDto, BindingResult  result) throws ParseException{
        log.info("Atualizando Ordem de Servico: {}", ordemServicoDto.toString());
        Response<OrdemServicoDTO> response = new Response<>();
        validarCliente(ordemServicoDto, result);
        validarEndereco(ordemServicoDto, result);
        validarTecnico(ordemServicoDto, result);
        ordemServicoDto.setId(id);

        Optional<OrdemServico> ordemServico = this.ordemServicoService.buscarPorId(id);

        if(!ordemServico.isPresent()){
            result.addError(new ObjectError("OrdemServicoService","Error Ordem de Servico não encontrada para o id "+id));
           // response.getErros().add("Error Ordem de Servico não encontrada para o id "+id);
            ResponseEntity.badRequest().body(response);
        }

        this.auditoria.post(this.converterOrdemServicoDto(ordemServico.get()), ordemServicoDto, "OrdemServicoService");
        ordemServico = Optional.of(this.converterDtoParaOrdemServico(ordemServicoDto, result));
      // ordemServico.get() = this.converterDtoParaOrdemServico(ordemServicoDto, result);

        if(result.hasErrors()){
            log.error("Erro ao validar ordem de servico: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        this.ordemServicoService.persistir(ordemServico.get());
        response.setData(this.converterOrdemServicoDto(ordemServico.get()));
        return ResponseEntity.ok(response);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id){
        log.info("Removendo ordem de serviço; {}", id);
        Response<String> response = new Response<>();
        Optional<OrdemServico> ordemServico = this.ordemServicoService.buscarPorId(id);

        if(!ordemServico.isPresent()){
            log.error("Erro ao remover a ordem de servico ID: {} ser invalido", id);
            response.getErros().add("Erro ao remover ordem de serviço. Registro não encotrado para o id "+id);
            return ResponseEntity.badRequest().body(response);
        }
        auditoria.post(this.converterOrdemServicoDto(ordemServico.get()), new OrdemServicoDTO(), "OrdemServicoService");
        this.ordemServicoService.remover(id);
        return ResponseEntity.ok(response);
    }





    private OrdemServico converterDtoParaOrdemServico(OrdemServicoDTO ordemServicoDto, BindingResult result) throws ParseException {

        OrdemServico ordemServico = new OrdemServico();

        ordemServico.setId(ordemServicoDto.getId());

        ordemServico.setCliente(new Cliente());
        ordemServico.getCliente().setId(ordemServicoDto.getCliente());

        ordemServico.setEndereco(new Endereco());
        ordemServico.getEndereco().setId(ordemServicoDto.getEndereco());

        ordemServico.setTecnico(new Tecnico());
        ordemServico.getTecnico().setId(ordemServicoDto.getCliente());


        ordemServico.setEspecificacao(ordemServicoDto.getEspecificacao());
        ordemServico.setDataAberta(this.dateFormat.parse(ordemServicoDto.getDataAberta()));
        if(ordemServicoDto.getDataFinalizada() != null )
            ordemServico.setDataFinalizada(this.dateFormat.parse(ordemServicoDto.getDataFinalizada()));

        if(EnumUtils.isValidEnum(Status.class, ordemServicoDto.getStatus())){
            ordemServico.setStatus(Status.valueOf(ordemServicoDto.getStatus()));
        }else{
            result.addError(new ObjectError("status","Status inválido"));
        }

        return ordemServico;

    }


    private void validarTecnico(OrdemServicoDTO ordemServicoDto, BindingResult result) {
        if(ordemServicoDto.getTecnico() == null){
            result.addError(new ObjectError("tecnico", "Tecnico não informado."));
            return;
        }

        log.info("Validando Tecnico id: {}", ordemServicoDto.getTecnico());
        Optional<Tecnico> tecnico = this.tecnicoService.buscarPorId(ordemServicoDto.getTecnico());
        if(!tecnico.isPresent()){
            result.addError(new ObjectError("tecnico", "Tecnico não encontrado. ID inexixtente."));
        }
    }


    private void validarEndereco(OrdemServicoDTO ordemServicoDto, BindingResult result) {
        if(ordemServicoDto.getEndereco() == null){
            result.addError(new ObjectError("endereco", "CEP do endereco não informado"));
            return;
        }

        log.info("Validando endereco cep: {}", ordemServicoDto.getEndereco());
        Optional<Endereco> endereco = this.enderecoService.buscarPorId(ordemServicoDto.getEndereco());
        if(!endereco.isPresent()){
            result.addError(new ObjectError("endereco", "Endereco não encontrado. CEP inexistente."));
        }
    }


    private void validarCliente(OrdemServicoDTO ordemServicoDto, BindingResult result) {
        if(ordemServicoDto.getCliente() == null){
            result.addError(new ObjectError("cliente", "Cliente não informado"));
            return;
        }

        try {
            log.info("Validando cliente id: {}", ordemServicoDto.getCliente());
            this.clienteService.buscarPorId(ordemServicoDto.getCliente());
        }catch(RuntimeException e){
            result.addError(new ObjectError("cliente", "Cliente não encontrado. ID inexixtente."));
        }
    }



    private OrdemServicoDTO converterOrdemServicoDto(OrdemServico ordemServico) {
        OrdemServicoDTO ordemServicoDto = new OrdemServicoDTO();

        ordemServicoDto.setId(ordemServico.getId());
        ordemServicoDto.setEspecificacao(ordemServico.getEspecificacao());
        ordemServicoDto.setStatus(ordemServico.getStatus().toString());
        ordemServicoDto.setDataAberta(this.dateFormat.format(ordemServico.getDataAberta()));
        if (ordemServico.getDataFinalizada() != null)
            ordemServicoDto.setDataFinalizada(this.dateFormat.format(ordemServico.getDataFinalizada()));
        ordemServicoDto.setCliente(ordemServico.getCliente().getId());
        ordemServicoDto.setEndereco(ordemServico.getEndereco().getId());
        ordemServicoDto.setTecnico(ordemServico.getTecnico().getId());

        return ordemServicoDto;
    }

}
