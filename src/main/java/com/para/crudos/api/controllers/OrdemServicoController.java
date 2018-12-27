package com.para.crudos.api.controllers;

import com.para.crudos.api.dtos.OrdemServicoDTO;
import com.para.crudos.api.services.OrdemServicoService;
import com.para.crudos.api.setup.UrlApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping(UrlApi.URL + "/os")
public class OrdemServicoController {
    private static final Logger log = LoggerFactory.getLogger(OrdemServicoController.class);

    @Autowired
    private OrdemServicoService ordemServicoService;

    @PostMapping("/cadastro-os")
    public ResponseEntity<OrdemServicoDTO> adicionar(@Valid @RequestBody OrdemServicoDTO ordemServicoDTO) {
        log.info("Adicionar uma Ordem de Serviço: {}", ordemServicoDTO.toString());

        return ResponseEntity.ok().body(this.ordemServicoService.gravar(ordemServicoDTO));
    }



    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<Page<OrdemServicoDTO>> listarPorClienteId(@PathVariable("clienteId") long clienteId,
                                                                              @RequestParam(value = "pag", defaultValue = "0") int pag,
                                                                              @RequestParam(value = "qtd", defaultValue = "10") int qtdItens,
                                                                              @RequestParam(value = "ord", defaultValue = "id") String ord,
                                                                              @RequestParam(value = "dir", defaultValue = "DESC") String dir){

        log.info("Buscando lançamento por ID de cliente: {}, página: {}", clienteId, pag);

        return ResponseEntity.ok().body(this.ordemServicoService.buscarPorClienteId(clienteId, pag, qtdItens,ord,dir));
    }



    @GetMapping("/{id}")
    public ResponseEntity<OrdemServicoDTO> listarporId(@PathVariable("id") Long id){
        log.info("Buscar orddem de servico por id: {}", id);

        return  ResponseEntity.ok().body(this.ordemServicoService.buscarPorId(id));
    }



    @PutMapping("/{id}")
    public ResponseEntity<OrdemServicoDTO> atualizar(@PathVariable("id") Long id,
                                                               @Valid @RequestBody OrdemServicoDTO ordemServicoDTO) {
        log.info("Atualizando Ordem de Servico: {}", ordemServicoDTO.toString());
        ordemServicoDTO.setId(id);

        return ResponseEntity.ok().body(this.ordemServicoService.atualizar(ordemServicoDTO));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> remover(@PathVariable("id") Long id){
        log.info("Removendo ordem de serviço; {}", id);
        return ResponseEntity.ok().body(this.ordemServicoService.remover(id));
    }


}
