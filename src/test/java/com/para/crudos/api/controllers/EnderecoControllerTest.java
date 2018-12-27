package com.para.crudos.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.para.crudos.api.dtos.EnderecoDTO;
import com.para.crudos.api.model.Endereco;
import com.para.crudos.api.repositories.EnderecoRepository;
import com.para.crudos.api.services.EnderecoService;
import com.para.crudos.api.setup.UrlApi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EnderecoControllerTest {


    @Autowired
    private MockMvc mvc;

    @Mock
    private EnderecoService enderecoService;

    @MockBean
    private EnderecoRepository enderecoRepository;


    @Autowired
    private ConversionService conversionService;

    private static final String URL_BASE = UrlApi.URL + "/enderecos";

    private static final Long ID = 1L;
    private static final String CEP = "68700-000";
    private static final String RUA = "Rua jão joão";
    private static final String BAIRRO = "Zona 7";
    private static final String CIDADE = "Maringá";
    private static final String ESTADO = "PR";



    @Test
    @DisplayName("Tentar cadastrar um novo endereco")
    public void testCadastarEndereco() throws Exception {
        Endereco endereco = this.obterDadosEndereco();
        doReturn(null).when(this.enderecoService).buscarPorCep(endereco.getCep());
        when(this.enderecoRepository.save(any())).thenReturn(endereco);
        mvc.perform(MockMvcRequestBuilders.post(URL_BASE + "/cadastro-endereco")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.obterJsonRequisitPost()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.cep").value(CEP));
    }

    @Test
    @DisplayName("Cadastrar endereco com CEP existente")
    public void testCadastrarEnderecoComCepExistente() throws Exception{
        Endereco endereco = this.obterDadosEndereco();
        doReturn(toDTO(endereco)).when(this.enderecoService).buscarPorCep(endereco.getCep());
        when(this.enderecoRepository.findByCep(endereco.getCep())).thenReturn(endereco);

        mvc.perform(MockMvcRequestBuilders.post(URL_BASE + "/cadastro-endereco")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.obterJsonRequisitPost()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagemDoErroGerado").value("endereco CEP já existente."));
    }

    @Test
    @DisplayName("Retorna um endereco dado um cep")
    public void testBuscarEnderecoPorCep() throws Exception{
        Endereco endereco = this.obterDadosEndereco();

        when(this.enderecoService.buscarPorCep(endereco.getCep())).thenReturn(this.conversionService.convert(endereco, EnderecoDTO.class));
        when(this.enderecoRepository.findByCep(endereco.getCep())).thenReturn(endereco);

        mvc.perform(MockMvcRequestBuilders.get(URL_BASE + "/cep/{cep}", CEP))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.cep").value(CEP));


    }

    @Test
    @DisplayName("Retornar erro 400 apos tentar buscar um endereco por cep invalido")
    public void testBuscarEnderecoPorCepInexistente() throws Exception{
        Endereco endereco = this.obterDadosEndereco();

        when(this.enderecoService.buscarPorCep(endereco.getCep())).thenReturn(this.conversionService.convert(endereco, EnderecoDTO.class));
        when(this.enderecoRepository.findByCep(endereco.getCep())).thenReturn(endereco);
        mvc.perform(MockMvcRequestBuilders.get(URL_BASE + "/cep/{cep}", "68300-000"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagemDoErroGerado").value("Endereco não encontrado para o CEP: 68300-000"));
    }


    @Test
    @DisplayName("Atualizar endereco dado um id")
    public void testAtualizarEnderecoPorId() throws Exception{
        Endereco endereco =  this.obterDadosEndereco();
        EnderecoDTO enderecoDTOAnt = this.conversionService.convert(endereco, EnderecoDTO.class);
        EnderecoDTO enderecoDTONov = this.conversionService.convert(obterDadosPut(), EnderecoDTO.class);
        when(this.enderecoService.atualizar(enderecoDTOAnt)).thenReturn(enderecoDTONov);
        when(this.enderecoRepository.findById(endereco.getId())).thenReturn(Optional.of(endereco));
        when(this.enderecoRepository.save(any())).thenReturn(obterDadosPut());

        mvc.perform(MockMvcRequestBuilders.put(URL_BASE + "/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.obterJsonRequisitPut()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.rua").value("Avenida Colombo"))
                .andExpect(jsonPath("$.cep").value(CEP));

    }

    @Test
    @DisplayName("Retorna err0 400 ao tentar atualizar endereco por id inexistente")
    public void testAtualizarEnderecoPorIdInexistente() throws Exception{
        Endereco endereco =  this.obterDadosEndereco();
        EnderecoDTO enderecoDTOAnt = this.conversionService.convert(endereco, EnderecoDTO.class);
        EnderecoDTO enderecoDTONov = this.conversionService.convert(obterDadosPut(), EnderecoDTO.class);
        when(this.enderecoService.atualizar(enderecoDTOAnt)).thenReturn(enderecoDTONov);
        when(this.enderecoRepository.findById(endereco.getId())).thenReturn(Optional.of(endereco));
        when(this.enderecoRepository.save(any())).thenReturn(obterDadosPut());

        mvc.perform(MockMvcRequestBuilders.put(URL_BASE + "/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.obterJsonRequisitPut()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagemDoErroGerado").value("Endereço não encontrado para o id: 2"));

    }

    @Test
    @DisplayName("Tentar excluir um endereco por id")
    public void testDeletarEnderecoPorId() throws Exception{
        Endereco endereco = this.obterDadosEndereco();

        when(this.enderecoService.remover(endereco.getId())).thenReturn(any());
        when(this.enderecoService.buscarPorId(endereco.getId())).thenReturn(endereco);
        when(this.enderecoRepository.findById(endereco.getId())).thenReturn(Optional.of(endereco));

        mvc.perform(MockMvcRequestBuilders.delete(URL_BASE + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Endereco deletado com sucesso......"));
    }


    @Test
    @DisplayName("Retornar erro 400 ao tentar excluir um endereco por id inexistente")
    public void testDeletarEnderecoPorIdInexistente() throws Exception{
        Endereco endereco = this.obterDadosEndereco();

        when(this.enderecoService.remover(endereco.getId())).thenReturn(any());
        when(this.enderecoService.buscarPorId(endereco.getId())).thenReturn(endereco);
        when(this.enderecoRepository.findById(endereco.getId())).thenReturn(Optional.of(endereco));

        mvc.perform(MockMvcRequestBuilders.delete(URL_BASE + "/{id}", 2))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagemDoErroGerado").value("Endereço não encontrado para o id: 2"));

    }



    private Endereco obterDadosEndereco(){
        Endereco endereco = new Endereco();

        endereco.setId(ID);
        endereco.setRua(RUA);
        endereco.setBairro(BAIRRO);
        endereco.setCidade(CIDADE);
        endereco.setEstado(ESTADO);
        endereco.setCep(CEP);

        return endereco;
    }

    private Endereco obterDadosPut(){
        Endereco endereco = new Endereco();

        endereco.setId(ID);
        endereco.setRua("Avenida Colombo");
        endereco.setBairro(BAIRRO);
        endereco.setCidade(CIDADE);
        endereco.setEstado(ESTADO);
        endereco.setCep(CEP);

        return endereco;
    }

    private EnderecoDTO toDTO(Endereco endereco){
        return this.conversionService.convert(endereco, EnderecoDTO.class);
    }

    private String obterJsonRequisitPut() throws JsonProcessingException {
        EnderecoDTO enderecoDto = new EnderecoDTO();

        enderecoDto.setId(null);
        enderecoDto.setRua("Avenida Colombo");
        enderecoDto.setBairro(BAIRRO);
        enderecoDto.setCidade(CIDADE);
        enderecoDto.setEstado(ESTADO);
        enderecoDto.setCep(CEP);
        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(enderecoDto);
    }


    private String obterJsonRequisitPost() throws JsonProcessingException {
        EnderecoDTO enderecoDto = new EnderecoDTO();

        enderecoDto.setId(null);
        enderecoDto.setRua(RUA);
        enderecoDto.setBairro(BAIRRO);
        enderecoDto.setCidade(CIDADE);
        enderecoDto.setEstado(ESTADO);
        enderecoDto.setCep(CEP);
        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(enderecoDto);
    }

}
