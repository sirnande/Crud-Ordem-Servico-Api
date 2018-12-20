package com.para.crudos.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.para.crudos.api.dtos.TecnicoDTO;
import com.para.crudos.api.model.Tecnico;
import com.para.crudos.api.services.TecnicoService;
import com.para.crudos.api.setup.UrlApi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static org.mockito.Mockito.doReturn;
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

    @MockBean
    private TecnicoService tecnicoService;

    private static final String URL_BASE = UrlApi.URL + "/tecnicos";
    private static final Long ID = 1l;
    private static final String NOME= "Amanda Lira";


    @Test
    @DisplayName("Salvar um novo tecnico")
    public void testSalvarTecnico() throws Exception{
        Tecnico tecnico = this.obterDadosTecnico();
        doReturn(Optional.empty()).when(this.tecnicoService).buscarPorId(tecnico.getId());
        mvc.perform(MockMvcRequestBuilders.post(URL_BASE + "/cadastro-tecnico")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.obterJsonRequisitPost()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nome").value(NOME))
                .andExpect(jsonPath("$.erros").isEmpty());
    }


    @Test
    @DisplayName("Buscar tecnico por id")
    public void testBuscarTecnicoPorId() throws Exception{

        Tecnico tecnico =  this.obterDadosTecnico();
        when(this.tecnicoService.buscarPorId(tecnico.getId())).thenReturn(Optional.of(tecnico));

        mvc.perform(MockMvcRequestBuilders.get(URL_BASE + "/id/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(ID))
                .andExpect(jsonPath("$.data.nome").value(NOME))
                .andExpect(jsonPath("$.erros").isEmpty());

    }

    @Test
    @DisplayName("Buscar um tecnico com id inexistente")
    public void testBuscarTecnicoPorIdInexistente() throws Exception{
        Tecnico tecnico = this.obterDadosTecnico();
        when(this.tecnicoService.buscarPorId(tecnico.getId())).thenReturn(Optional.of(tecnico));

        mvc.perform(MockMvcRequestBuilders.get(URL_BASE + "/id/{id}", 2))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros").value("Tecnico não encontrado para o ID 2"))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    @DisplayName("Atualizar tecnico pelo id")
    public void testAtualizarClientePorId() throws Exception{
        Tecnico tecnico = this.obterDadosTecnico();

        when(this.tecnicoService.buscarPorId(tecnico.getId())).thenReturn(Optional.of(tecnico));

        mvc.perform(MockMvcRequestBuilders.put(URL_BASE + "/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.obterJsonRequisitPut()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nome").value("Romulo Maiorama"))
                .andExpect(jsonPath("$.erros").isEmpty());
    }

    @Test
    @DisplayName("Atualizar tecnico com id inexistente")
    public void testAtualizarTecnicoPorIdInexistente() throws Exception{
        Tecnico tecnico = this.obterDadosTecnico();

        when(this.tecnicoService.buscarPorId(tecnico.getId())).thenReturn(Optional.of(tecnico));

        mvc.perform(MockMvcRequestBuilders.put(URL_BASE + "/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.obterJsonRequisitPut()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros").value("Erro tecnico não encontrado para o id 2"))
                .andExpect(jsonPath("$.data").isEmpty());

    }




    @Test
    @DisplayName("Excluir um tecnico pelo id")
    public void testDeletartecnicoPeloId() throws Exception{
        Tecnico tecnico = this.obterDadosTecnico();

        when(this.tecnicoService.buscarPorId(tecnico.getId())).thenReturn(Optional.of(tecnico));

        mvc.perform(MockMvcRequestBuilders.delete(URL_BASE + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.erros").isEmpty());
    }

    @Test
    @DisplayName("Excluir um tecnico pelo id inexistente")
    public void testDeletarTecnicoPeloIdInexistente() throws Exception{
        Tecnico tecnico = this.obterDadosTecnico();

        when(this.tecnicoService.buscarPorId(tecnico.getId())).thenReturn(Optional.of(tecnico));

        mvc.perform(MockMvcRequestBuilders.delete(URL_BASE + "/{id}", 2))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros").value("Erro ao excluir o cliente. Registro não encontrado para o id 2"))
                .andExpect(jsonPath("$.data").isEmpty());
    }


    private Tecnico obterDadosTecnico(){
        Tecnico tecnico = new Tecnico();
        tecnico.setId(ID);
        tecnico.setNome(NOME);

        return tecnico;
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
