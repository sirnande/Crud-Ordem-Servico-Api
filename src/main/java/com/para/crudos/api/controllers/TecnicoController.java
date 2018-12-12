package com.para.crudos.api.controllers;

import com.para.crudos.api.auditoria.Auditoria;
import com.para.crudos.api.dtos.TecnicoDto;
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

    private Auditoria<TecnicoDto> auditoria = new Auditoria<>();


    /**
     * Método responsável por cadastrar um novo tecnico
     *
     * @param tecnicoDto
     * @param result
     * @return ResponseEntity<Response<TecnicoDto>>
     */
    @PostMapping("/cadastro-tecnico")
    public ResponseEntity<Response<TecnicoDto>> cadastrar(@Valid @RequestBody TecnicoDto tecnicoDto, BindingResult result){
        log.info("Cadastrando um novo Técnico: {}", tecnicoDto.toString());

        Response<TecnicoDto> response = new Response<>();

        validarDadosExistentes(tecnicoDto, result);
        Tecnico tecnico = this.converterDtoParaTecnico(tecnicoDto, result);

        if (result.hasErrors()){
           log.error("Erro ao validar dados do tecnico: {}", result.getAllErrors());
           result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
           return ResponseEntity.badRequest().body(response);
        }

        auditoria.post(new TecnicoDto(), this.converterCadastroTecnicoDto(tecnico), "Tecnico");
        this.tecnicoService.persistir(tecnico);
        response.setData(this.converterCadastroTecnicoDto(tecnico));
        return ResponseEntity.ok(response);

    }



    /**
     * Método responsável por buscar um técnico dado im determinado id
     *
     * @param id
     * @return ResponseEntity<Response<TecnicoDto>>
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<Response<TecnicoDto>> buscarPorNome(@PathVariable("id") Long id){
        log.info("Retorna um tecnico prlo id: {}", id);

        Response<TecnicoDto> response = new Response<>();
        Optional<Tecnico> tecnico = this.tecnicoService.buscarPorId(id);

        if(!tecnico.isPresent()){
            log.error("Tecnico não encotrado: {}", id);
            return ResponseEntity.badRequest().body(response);
        }

        response.setData(this.converterCadastroTecnicoDto(tecnico.get()));
        return ResponseEntity.ok(response);
    }


    /**
     * Método responsável por atualizar um tecnico
     *
     * @param id
     * @param tecnicoDto
     * @param result
     * @return ResponseEntity<Response<TecnicoDto>>
     * @throws ParseException
     */
    @PutMapping("/{id}")
    public ResponseEntity<Response<TecnicoDto>> atualizar(@PathVariable("id") Long id,
                                                          @Valid @RequestBody TecnicoDto tecnicoDto, BindingResult  result) throws ParseException {
        log.info("Atualizar tecnico: {}", id);
        Response<TecnicoDto> response = new Response<>();
        validarDadosExistentes(tecnicoDto, result);

        Optional<Tecnico> tecnico =  this.tecnicoService.buscarPorId(id);
        if(!tecnico.isPresent()){
            result.addError(new ObjectError("Tecnico", "Erro tecnico não encontrado"));
        }

        auditoria.post(this.converterCadastroTecnicoDto(tecnico.get()), tecnicoDto, "Tecnico");

        this.atualizarDadosTecnico(tecnico.get(), tecnicoDto, result);

        if(result.hasErrors()){
            log.error("Erro ao remover um tecnico com id: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        Tecnico tecnicos = this.tecnicoService.persistir(tecnico.get());
        response.setData(this.converterCadastroTecnicoDto(tecnicos));
        return ResponseEntity.ok(response);
    }



    /**
     * Método responsável por deletra um tecnico
     *
     * @param id
     * @return esponseEntity<Response<String>>
     */
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
        auditoria.post(this.converterCadastroTecnicoDto(tecnico.get()), new TecnicoDto(), "Tecnico");
        this.tecnicoService.remover(id);
        return ResponseEntity.ok(response);
    }

    private void atualizarDadosTecnico(Tecnico tecnico, TecnicoDto tecnicoDto, BindingResult result) {
        tecnico.setNome(tecnicoDto.getNome());
    }



    /**
     * Método responsável por converter entidade Tecnico me TecnicoDto
     *
     * @param tecnico
     * @return
     */
    private TecnicoDto converterCadastroTecnicoDto(Tecnico tecnico) {

        TecnicoDto tecnicoDto = new TecnicoDto();

        tecnicoDto.setNome(tecnico.getNome());

        return tecnicoDto;
    }


    /**
     * Método responsálvel por converter objeto DTO para Tecnico
     *
     * @param tecnicoDto
     * @param result
     * @return Tecnico
     */
    private Tecnico converterDtoParaTecnico(TecnicoDto tecnicoDto, BindingResult result) {
        Tecnico tecnico = new Tecnico();

        tecnico.setNome(tecnicoDto.getNome());

        return tecnico;
    }


    /**
     * Método responsável por verificar se os dados já existem no banco de dado
     *
     * @param tecnicoDto
     * @param result
     */
    private void validarDadosExistentes(TecnicoDto tecnicoDto, BindingResult result) {
        this.tecnicoService.buscarPorNome(tecnicoDto.getNome())
                .ifPresent(emp -> result.addError(new ObjectError("tecnico", "NOME já existente")));
    }
}
