package com.para.crudos.api.controllers;

import com.para.crudos.api.auditoria.Auditoria;
import com.para.crudos.api.dtos.ClienteDTO;
import com.para.crudos.api.model.Cliente;
import com.para.crudos.api.services.ClienteService;
import com.para.crudos.api.setup.UrlApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
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

    @Autowired
    private ConversionService conversionService;

    private Auditoria<ClienteDTO> auditoria =  new Auditoria<>();


    @PostMapping("/cadastro-cliente")
    public ResponseEntity<ClienteDTO> cadastrar(@Valid @RequestBody ClienteDTO clienteDto){
        log.info("Cadastrando cliente: {}", clienteDto.toString());

        this.clienteService.validarDadosExistentes(clienteDto);

       // auditoria.post(new ClienteDTO(), clienteDto, "Cliente");

        this.clienteService.gravar(this.conversionService.convert(clienteDto, Cliente.class));


        return ResponseEntity.ok(clienteDto);
    }



    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<ClienteDTO> buscarPorCpf(@PathVariable("cpf") String cpf){
        log.info("Buscando clinete por cpf: {}", cpf);
        Cliente cliente = this.clienteService.buscarPorCpf(cpf);
        return ResponseEntity.ok(this.conversionService.convert(cliente, ClienteDTO.class));
    }



    @PutMapping("/id/{id}")
    public ResponseEntity<ClienteDTO> atualizar(@PathVariable("id") Long id,
                                                @Valid @RequestBody ClienteDTO clienteDto) throws NoSuchAlgorithmException{
        log.info("Atualizar cliente: {}", clienteDto.toString());
        this.clienteService.buscarPorId(id);
        //auditoria.post(this.conversionService.convert(this.clienteService.buscarPorId(id), ClienteDTO.class), clienteDto, "Cliente");

        clienteDto.setId(id);
        this.clienteService.atualizar(this.conversionService.convert(clienteDto, Cliente.class));

       return ResponseEntity.ok(clienteDto);

    }


    @DeleteMapping("/id/{id}")
    public ResponseEntity<String> deletar(@PathVariable("id") Long id){
        log.info("Removendo um cliente pelo id; {}", id);

        Cliente cliente = this.clienteService.buscarPorId(id);
        //auditoria.post(this.clienteService.converterCadastroClienteDto(cliente), new ClienteDTO(), "Cliente");
        this.clienteService.remover(id);

        return  ResponseEntity.ok("Cliente removido com sucesso...");

    }
//
//    public ClienteDTO converterCadastroClienteDto(Cliente cliente) {
//        ClienteDTO clienteDto = new ClienteDTO();
//
//        clienteDto.setId(cliente.getId());
//        clienteDto.setNome(cliente.getNome());
//        clienteDto.setCpf(cliente.getCpf());
//        clienteDto.setEmail(cliente.getEmail());
//        clienteDto.setTelefone(cliente.getTelefone());
//
//        return clienteDto;
//    }
//
//    public Cliente converterDtoParaCliente(ClienteDTO clienteDto){
//        Cliente cliente = new Cliente();
//
//        cliente.setId(clienteDto.getId());
//        cliente.setNome(clienteDto.getNome());
//        cliente.setCpf(clienteDto.getCpf());
//        cliente.setEmail(clienteDto.getEmail());
//        cliente.setTelefone(clienteDto.getTelefone());
//
//        return cliente;
//    }

}
