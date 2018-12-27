package com.para.crudos.api.controllers;

import com.para.crudos.api.dtos.TecnicoDTO;
import com.para.crudos.api.services.TecnicoService;
import com.para.crudos.api.setup.UrlApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(UrlApi.URL+"/tecnicos")
public class TecnicoController {

    public static final Logger log  = LoggerFactory.getLogger(TecnicoController.class);

    @Autowired
    private TecnicoService tecnicoService;


    @PostMapping("/cadastro-tecnico")
    public ResponseEntity<TecnicoDTO> cadastrar(@Valid @RequestBody TecnicoDTO tecnicoDto){
        log.info("Cadastrando um novo Técnico: {}", tecnicoDto.toString());
        return ResponseEntity.ok().body(this.tecnicoService.salvar(tecnicoDto));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<TecnicoDTO> buscarPorId(@PathVariable("id") Long id){
        log.info("Retorna um tecnico pelo id: {}", id);
          return ResponseEntity.ok().body(this.tecnicoService.buscarPorId(id));
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<TecnicoDTO> buscarPorNome(@PathVariable("nome") String nome){
        log.info("Buscar tecnico pelo nome: {}", nome);
        return ResponseEntity.ok().body(this.tecnicoService.buscarPorNome(nome));
    }


    @PutMapping("/{id}")
    public ResponseEntity<TecnicoDTO> atualizar(@PathVariable("id") Long id, @Valid @RequestBody TecnicoDTO tecnicoDto){
        log.info("Atualizar tecnico: {}", id);
        tecnicoDto.setId(id);
        return ResponseEntity.ok().body(this.tecnicoService.atualizar(tecnicoDto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable("id") Long id){
        log.info("Excluindo um cliente pelo id: {}", id);
        return ResponseEntity.ok().body(this.tecnicoService.remover(id));
    }

}
