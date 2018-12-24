package com.para.crudos.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.para.crudos.api.dtos.TecnicoDTO;
import com.para.crudos.api.model.Tecnico;
import com.para.crudos.api.repositories.TecnicoRepository;
import com.para.crudos.api.services.TecnicoService;
import com.para.crudos.api.setup.UrlApi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class TecnicoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Mock
    private TecnicoService tecnicoService;

    @MockBean
    private TecnicoRepository tecnicoRepository;

    private static final String URL_BASE = UrlApi.URL + "/tecnicos";
    private static final Long ID = 1l;
    private static final String NOME= "Amanda Lira";


    @Test
    @DisplayName("Salvar um novo tecnico")
    public void testSalvarTecnico() throws Exception{
        Tecnico tecnico = obterDadosTecnico();
        TecnicoDTO tecnicoDTO = this.obterDadosDto(this.obterDadosTecnico());
        TecnicoDTO tecnicoDTOPost = obterDadosDtoPost(this.obterDadosTecnico());

        when(this.tecnicoService.salvar(tecnicoDTOPost)).thenReturn(tecnicoDTO);
        when(this.tecnicoRepository.save(any())).thenReturn(tecnico);

        mvc.perform(MockMvcRequestBuilders.post(URL_BASE + "/cadastro-tecnico")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.obterJsonRequisitPost()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.nome").value(NOME));
    }


    @Test
    @DisplayName("Buscar tecnico por id")
    public void testBuscarTecnicoPorId() throws Exception{

        TecnicoDTO tecnicoDTO =  this.obterDadosDto(obterDadosTecnico());
        when(this.tecnicoService.buscarPorId(tecnicoDTO.getId())).thenReturn(tecnicoDTO);
        when(this.tecnicoRepository.findById(tecnicoDTO.getId())).thenReturn(Optional.of(obterDadosTecnico()));


        mvc.perform(MockMvcRequestBuilders.get(URL_BASE + "/id/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.nome").value(NOME));

    }

    @Test
    @DisplayName("Buscar um tecnico com id inexistente")
    public void testBuscarTecnicoPorIdInexistente() throws Exception{
        TecnicoDTO tecnicoDto = this.obterDadosDto(obterDadosTecnico());
        when(this.tecnicoService.buscarPorId(tecnicoDto.getId())).thenReturn(tecnicoDto);

        mvc.perform(MockMvcRequestBuilders.get(URL_BASE + "/id/{id}", 2))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagemDoErroGerado").value("Tecnico não encotrado para o id: 2"));

    }

    @Test
    @DisplayName("Atualizar tecnico pelo id")
    public void testAtualizarClientePorId() throws Exception{
        TecnicoDTO tecnicoDto = this.obterDadosDto(obterDadosTecnico());

        when(this.tecnicoService.buscarPorId(tecnicoDto.getId())).thenReturn(tecnicoDto);
        when(this.tecnicoService.atualizar(tecnicoDto)).thenReturn(this.obterDadosDtoPut(obterDadosTecnico()));
        when(this.tecnicoRepository.findById(tecnicoDto.getId())).thenReturn(Optional.of(obterDadosTecnico()));
        when(this.tecnicoRepository.save(any())).thenReturn(TecnicoDTOToTecnico(obterDadosDtoPut(obterDadosTecnico())));

        mvc.perform(MockMvcRequestBuilders.put(URL_BASE + "/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.obterJsonRequisitPut()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.nome").value("Romulo Maiorama"));
    }

    @Test
    @DisplayName("Atualizar tecnico com id inexistente")
    public void testAtualizarTecnicoPorIdInexistente() throws Exception{
        TecnicoDTO tecnicoDto = this.obterDadosDto(obterDadosTecnico());

        when(this.tecnicoService.buscarPorId(tecnicoDto.getId())).thenReturn(tecnicoDto);

        mvc.perform(MockMvcRequestBuilders.put(URL_BASE + "/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.obterJsonRequisitPut()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagemDoErroGerado").value("Tecnico não encotrado para o id: 2"));

    }




    @Test
    @DisplayName("Excluir um tecnico pelo id")
    public void testDeletartecnicoPeloId() throws Exception{
        TecnicoDTO tecnicoDto = this.obterDadosDto(obterDadosTecnico());

        when(this.tecnicoService.remover(tecnicoDto.getId())).thenReturn(any());
        when(this.tecnicoService.buscarPorId(tecnicoDto.getId())).thenReturn(tecnicoDto);
        when(this.tecnicoRepository.findById(tecnicoDto.getId())).thenReturn(Optional.of(obterDadosTecnico()));


        mvc.perform(MockMvcRequestBuilders.delete(URL_BASE + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Tecnico excluido com sucesso..."));
    }

    @Test
    @DisplayName("Excluir um tecnico pelo id inexistente")
    public void testDeletarTecnicoPeloIdInexistente() throws Exception{
        TecnicoDTO tecnicoDto = this.obterDadosDto(obterDadosTecnico());

        when(this.tecnicoService.remover(tecnicoDto.getId())).thenReturn(any());
        when(this.tecnicoService.buscarPorId(tecnicoDto.getId())).thenReturn(tecnicoDto);
        when(this.tecnicoRepository.findById(tecnicoDto.getId())).thenReturn(Optional.of(obterDadosTecnico()));

        mvc.perform(MockMvcRequestBuilders.delete(URL_BASE + "/{id}", 2))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagemDoErroGerado").value("Tecnico não encotrado para o id: 2"));
    }


    private Tecnico obterDadosTecnico(){
        Tecnico tecnico = new Tecnico();
        tecnico.setId(ID);
        tecnico.setNome(NOME);

        return tecnico;
    }

    private TecnicoDTO obterDadosDto(Tecnico tecnico){
        TecnicoDTO tecnicoDto = new TecnicoDTO();

        tecnicoDto.setId(tecnico.getId());
        tecnicoDto.setNome(tecnico.getNome());

        return tecnicoDto;
    }

    private TecnicoDTO obterDadosDtoPost(Tecnico tecnico){
        TecnicoDTO tecnicoDto = new TecnicoDTO();

        tecnicoDto.setNome(tecnico.getNome());

        return tecnicoDto;
    }

    private Tecnico TecnicoDTOToTecnico(TecnicoDTO tecnicoDTO){
        Tecnico tecnico = new Tecnico();
        tecnico.setId(tecnicoDTO.getId());
        tecnico.setNome(tecnicoDTO.getNome());

        return tecnico;
    }

    private TecnicoDTO obterDadosDtoPut(Tecnico tecnico){
        TecnicoDTO tecnicoDto = new TecnicoDTO();
        tecnicoDto.setId(tecnico.getId());
        tecnicoDto.setNome("Romulo Maiorama");

        return tecnicoDto;
    }

    private String obterJsonRequisitPost() throws JsonProcessingException{
        TecnicoDTO tecnicoDto = new TecnicoDTO();

        tecnicoDto.setId(null);
        tecnicoDto.setNome(NOME);

        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(tecnicoDto);
    }

    private String obterJsonRequisitPut() throws JsonProcessingException{
        Tecnico  tecnico = new Tecnico();
        tecnico.setId(null);
        tecnico.setNome("Romulo Maiorama");
        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(tecnico);
    }
}
