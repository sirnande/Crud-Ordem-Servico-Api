package com.para.crudos.api.controllers;

import com.para.crudos.api.auditoria.Auditoria;
import com.para.crudos.api.dtos.EnderecoDTO;
import com.para.crudos.api.model.Endereco;
import com.para.crudos.api.services.EnderecoService;
import com.para.crudos.api.setup.UrlApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(UrlApi.URL+"/enderecos")
public class EnderecoController {

    private static final Logger log = LoggerFactory.getLogger(EnderecoController.class);

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private ConversionService conversionService;

    @PostMapping("/cadastro-endereco")
    public ResponseEntity<EnderecoDTO> cadastrar(@Valid @RequestBody EnderecoDTO enderecoDto) throws NoSuchAlgorithmException{
        log.info("Cadastrando um novo endereço: {}", enderecoDto.toString());
        //auditoria.post(new EnderecoDTO(), enderecoDto, "Endereco");
        return ResponseEntity.ok().body(this.enderecoService.salvar(enderecoDto));
    }


    @GetMapping("/cep/{cep}")
    public ResponseEntity<EnderecoDTO> buscarPorCep(@PathVariable("cep") String cep){
        log.info("Buscando endereco pelo cep: {}", cep);
        return ResponseEntity.ok().body(this.enderecoService.buscarPorCep(cep));
    }

    @GetMapping("/rua/{rua}")
    public ResponseEntity<List<EnderecoDTO>> buscarPorRua(@PathVariable("rua") String rua){
        log.info("Buscando endereco pela rua: {}", rua);
        return ResponseEntity.ok().body(this.enderecoService.buscarPorRua(rua));
    }

    @GetMapping("/cidade/{cidade}")
    public ResponseEntity<List<EnderecoDTO>> buscarPOrCidade(@PathVariable("cidade") String cidade){
        log.info("Buscando endereco pela cidade: {}", cidade);
        return ResponseEntity.ok().body(this.enderecoService.buscarPorCidade(cidade));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnderecoDTO> atualizar(@PathVariable("id") Long id,
                                                           @Valid @RequestBody EnderecoDTO enderecoDto) throws ParseException{
        log.info("Atualizando Enderco : {}", enderecoDto.toString());
        enderecoDto.setId(id);
        return ResponseEntity.ok().body(this.enderecoService.atualizar(enderecoDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable("id") Long id){
        log.info("Removendo um endereco: {}", id);
        return ResponseEntity.ok().body(this.enderecoService.remover(id));
    }

}
