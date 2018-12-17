package com.para.crudos.api.controllers;

import com.para.crudos.api.auditoria.Auditoria;
import com.para.crudos.api.dtos.EnderecoDto;
import com.para.crudos.api.model.Endereco;
import com.para.crudos.api.response.Response;
import com.para.crudos.api.services.EnderecoService;
import com.para.crudos.api.setup.UrlApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Optional;

@RestController
@RequestMapping(UrlApi.URL+"/enderecos")
public class EnderecoController {

    private static final Logger log = LoggerFactory.getLogger(EnderecoController.class);

    @Autowired
    private EnderecoService enderecoService;


    private Auditoria<EnderecoDto> auditoria = new Auditoria<>();

    @PostMapping("/cadastro-endereco")
    public ResponseEntity<Response<EnderecoDto>> cadastrar(@Valid @RequestBody EnderecoDto enderecoDto,
                                                           BindingResult result) throws NoSuchAlgorithmException{
        log.info("Cadastrando um novo endereço: {}", enderecoDto.toString());

        Response<EnderecoDto> response = new Response<>();

        validarDadosExistentes(enderecoDto, result);
        Endereco endereco = this.converteDtoParaEndereco(enderecoDto, result);

        if(result.hasErrors()){
            log.error("Erro validando dados do endereco: {}",result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        auditoria.post(new EnderecoDto(), enderecoDto, "Endereco");

        this.enderecoService.persistir(endereco);
        response.setData((this.converterCadastroEnderecoDto(endereco)));
        return ResponseEntity.ok(response);
    }


    @GetMapping("/cep/{cep}")
    public ResponseEntity<Response<EnderecoDto>> buscarPorCep(@PathVariable("cep") String cep){
        log.info("Buscando endereco pelo cep: {}", cep);

        Response<EnderecoDto> response =  new Response<>();
        Optional<Endereco> endereco = this.enderecoService.buscarPorCep(cep);

        if(!endereco.isPresent()){
            log.info("CEP não encontrado: {}", cep);
            response.getErros().add("CEP não encontrado: "+cep);
            return ResponseEntity.badRequest().body(response);
        }

        response.setData(this.converterCadastroEnderecoDto(endereco.get()));

        return ResponseEntity.ok(response);
    }



    @PutMapping("/{id}")
    public ResponseEntity<Response<EnderecoDto>> atualizar(@PathVariable("id") Long id,
                                                        @Valid @RequestBody EnderecoDto enderecoDto, BindingResult  result) throws ParseException{
        log.info("Atualizando Enderco : {}", enderecoDto.toString());

        Response<EnderecoDto> response =  new Response<>();
        validarDadosExistentes(enderecoDto, result);

        Optional<Endereco> endereco = this.enderecoService.buscarPorId(id);

        if(!endereco.isPresent()){
            result.addError(new ObjectError("endereco", "Error endereco não encontrado para o id: "+id));
            response.getErros().add("Error endereco não encontrado para o id: "+ id);
            return ResponseEntity.badRequest().body(response);
        }

        auditoria.post(this.converterCadastroEnderecoDto(endereco.get()), enderecoDto, "Endereco");
        this.atualizarDadosEndereco(endereco.get(), enderecoDto, result);

        if(result.hasErrors()){
            log.error("Erro ao validar endereco: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        this.enderecoService.persistir(endereco.get());
        response.setData(this.converterCadastroEnderecoDto(endereco.get()));
        return ResponseEntity.ok(response);

    }




    @DeleteMapping("/{id}")
    public ResponseEntity<Response<String>> deletar(@PathVariable("id") Long id){
        log.info("Removendo um endereco: {}", id);

        Response<String> response = new Response<>();
        Optional<Endereco> endereco = this.enderecoService.buscarPorId(id);

        if(!endereco.isPresent()){
            log.error("Erro o excluir um endereco ID: {} ser inválido", id);
            response.getErros().add("Erro ao excluir um endereco. Endereco não encontrado para o id "+id);
            return ResponseEntity.badRequest().body(response);
        }

        auditoria.post(this.converterCadastroEnderecoDto(endereco.get()), new EnderecoDto(), "Endereco");
        this.enderecoService.remover(id);
        return ResponseEntity.ok(response);
    }




    private void atualizarDadosEndereco(Endereco endereco, EnderecoDto enderecoDto, BindingResult result) {

        if(!endereco.getCep().equals(enderecoDto.getCep()))
            endereco.setCep(enderecoDto.getCep());
        endereco.setEstado(enderecoDto.getEstado());
        endereco.setCidade(enderecoDto.getCidade());
        endereco.setBairro(enderecoDto.getBairro());
        endereco.setRua(enderecoDto.getRua());
    }



    private EnderecoDto converterCadastroEnderecoDto(Endereco endereco) {
        EnderecoDto enderecoDto = new EnderecoDto();

        enderecoDto.setId(endereco.getId());
        enderecoDto.setCep(endereco.getCep());
        enderecoDto.setRua(endereco.getRua());
        enderecoDto.setBairro(endereco.getBairro());
        enderecoDto.setCidade(endereco.getCidade());
        enderecoDto.setEstado(endereco.getEstado());

        return enderecoDto;
    }


    private Endereco converteDtoParaEndereco(EnderecoDto enderecoDto, BindingResult result) {
        Endereco endereco = new Endereco();

        endereco.setCep(enderecoDto.getCep());
        endereco.setRua(enderecoDto.getRua());
        endereco.setBairro(enderecoDto.getBairro());
        endereco.setCidade(enderecoDto.getCidade());
        endereco.setEstado(enderecoDto.getEstado());

        return endereco;
    }


    private void validarDadosExistentes(EnderecoDto enderecoDto, BindingResult result) {
        this.enderecoService.buscarPorCep(enderecoDto.getCep())
                .ifPresent(emp -> result.addError(new ObjectError("endereco", "CEP já existente.")));

    }


}
