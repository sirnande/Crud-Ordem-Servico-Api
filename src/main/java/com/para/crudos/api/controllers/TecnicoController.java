package com.para.crudos.api.controllers;

import com.para.crudos.api.auditoria.Auditoria;
import com.para.crudos.api.dtos.TecnicoDTO;
import com.para.crudos.api.model.Tecnico;
import com.para.crudos.api.response.Response;
import com.para.crudos.api.services.TecnicoService;
import com.para.crudos.api.setup.UrlApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.Optional;

@RestController
@RequestMapping(UrlApi.URL+"/tecnicos")
public class TecnicoController {

    public static final Logger log  = LoggerFactory.getLogger(TecnicoController.class);

    @Autowired
    private TecnicoService tecnicoService;

    private Auditoria<TecnicoDTO> auditoria = new Auditoria<>();


    @PostMapping("/cadastro-tecnico")
    public ResponseEntity<Response<TecnicoDTO>> cadastrar(@Valid @RequestBody TecnicoDTO tecnicoDto, BindingResult result){
        log.info("Cadastrando um novo Técnico: {}", tecnicoDto.toString());

        Response<TecnicoDTO> response = new Response<>();

        validarDadosExistentes(tecnicoDto, result);
        Tecnico tecnico = this.converterDtoParaTecnico(tecnicoDto, result);

        if (result.hasErrors()){
           log.error("Erro ao validar dados do tecnico: {}", result.getAllErrors());
           result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
           return ResponseEntity.badRequest().body(response);
        }

        auditoria.post(new TecnicoDTO(), this.converterCadastroTecnicoDto(tecnico), "Tecnico");
        this.tecnicoService.persistir(tecnico);
        response.setData(this.converterCadastroTecnicoDto(tecnico));
        return ResponseEntity.ok(response);

    }




    @GetMapping("/id/{id}")
    public ResponseEntity<Response<TecnicoDTO>> buscarPorNome(@PathVariable("id") Long id){
        log.info("Retorna um tecnico prlo id: {}", id);

        Response<TecnicoDTO> response = new Response<>();
        Optional<Tecnico> tecnico = this.tecnicoService.buscarPorId(id);

        if(!tecnico.isPresent()){
            log.error("Tecnico não encotrado: {}", id);
            response.getErros().add("Tecnico não encontrado para o ID "+ id);
            return ResponseEntity.badRequest().body(response);
        }

        response.setData(this.converterCadastroTecnicoDto(tecnico.get()));
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Response<TecnicoDTO>> atualizar(@PathVariable("id") Long id,
                                                          @Valid @RequestBody TecnicoDTO tecnicoDto, BindingResult  result) throws ParseException {
        log.info("Atualizar tecnico: {}", id);
        Response<TecnicoDTO> response = new Response<>();
        validarDadosExistentes(tecnicoDto, result);

        Optional<Tecnico> tecnico =  this.tecnicoService.buscarPorId(id);
        if(!tecnico.isPresent()){
            result.addError(new ObjectError("Tecnico", "Erro tecnico não encontrado"));
            response.getErros().add("Erro tecnico não encontrado para o id "+id);
            return ResponseEntity.badRequest().body(response);
        }

        auditoria.post(this.converterCadastroTecnicoDto(tecnico.get()), tecnicoDto, "Tecnico");

        this.atualizarDadosTecnico(tecnico.get(), tecnicoDto, result);

        if(result.hasErrors()){
            log.error("Erro ao remover um tecnico com id: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

       this.tecnicoService.persistir(tecnico.get());
        response.setData(this.converterCadastroTecnicoDto(tecnico.get()));
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Response<String>> deletar(@PathVariable("id") Long id){
        log.info("Excluindo um cliente pelo id: {}", id);
        Response<String> response = new Response<>();
        Optional<Tecnico> tecnico = this.tecnicoService.buscarPorId(id);

        if(!tecnico.isPresent()){
            log.error("Erro ao excluir o clientent ID: {} ser inválido", id);
            response.getErros().add("Erro ao excluir o cliente. Registro não encontrado para o id "+id);
            return ResponseEntity.badRequest().body(response);
        }
        auditoria.post(this.converterCadastroTecnicoDto(tecnico.get()), new TecnicoDTO(), "Tecnico");
        this.tecnicoService.remover(id);
        return ResponseEntity.ok(response);
    }





    private void atualizarDadosTecnico(Tecnico tecnico, TecnicoDTO tecnicoDto, BindingResult result) {
        tecnico.setNome(tecnicoDto.getNome());
    }



    private TecnicoDTO converterCadastroTecnicoDto(Tecnico tecnico) {

        TecnicoDTO tecnicoDto = new TecnicoDTO();

        tecnicoDto.setId(tecnico.getId());
        tecnicoDto.setNome(tecnico.getNome());

        return tecnicoDto;
    }



    private Tecnico converterDtoParaTecnico(TecnicoDTO tecnicoDto, BindingResult result) {
        Tecnico tecnico = new Tecnico();

        tecnico.setNome(tecnicoDto.getNome());

        return tecnico;
    }


    private void validarDadosExistentes(TecnicoDTO tecnicoDto, BindingResult result) {
        this.tecnicoService.buscarPorNome(tecnicoDto.getNome())
                .ifPresent(emp -> result.addError(new ObjectError("tecnico", "NOME já existente")));
    }
}
