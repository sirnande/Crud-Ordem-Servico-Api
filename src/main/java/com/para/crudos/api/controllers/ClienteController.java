package com.para.crudos.api.controllers;

import com.para.crudos.api.dtos.ClienteDTO;
import com.para.crudos.api.services.ClienteService;
import com.para.crudos.api.setup.UrlApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping(UrlApi.URL+"/clientes")
public class ClienteController {
    private static final Logger log = LoggerFactory.getLogger(ClienteController.class);


    @Autowired
    private ClienteService clienteService;

    @PostMapping("/cadastro-cliente")
    public ResponseEntity<ClienteDTO> cadastrar(@Valid @RequestBody ClienteDTO clienteDto){
        log.info("Cadastrando cliente: {}", clienteDto.toString());
        return ResponseEntity.ok().body(this.clienteService.gravar(clienteDto));
    }



    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<ClienteDTO> buscarPorCpf(@PathVariable("cpf") String cpf){
        log.info("Buscando clinete por cpf: {}", cpf);
        return ResponseEntity.ok().body(this.clienteService.buscarPorCpf(cpf));
    }



    @PutMapping("/id/{id}")
    public ResponseEntity<ClienteDTO> atualizar(@PathVariable("id") Long id,
                                                @Valid @RequestBody ClienteDTO clienteDto){
        log.info("Atualizar cliente: {}", clienteDto.toString());
       return ResponseEntity.ok().body(this.clienteService.atualizar(clienteDto));
    }


    @DeleteMapping("/id/{id}")
    public ResponseEntity<String> deletar(@PathVariable("id") Long id){
        log.info("Removendo um cliente pelo id; {}", id);
        return  ResponseEntity.ok().body(this.clienteService.remover(id));
    }


}
