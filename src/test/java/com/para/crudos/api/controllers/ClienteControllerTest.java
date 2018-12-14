package com.para.crudos.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.para.crudos.api.dtos.ClienteDto;
import com.para.crudos.api.model.Cliente;
import com.para.crudos.api.services.ClienteService;
import com.para.crudos.api.setup.UrlApi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ClienteControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ClienteService clienteService;


    private static final String URL_BASE = UrlApi.URL+"/clientes";
    private static final Long ID =  1L;
    private static final String CPF = "01834275210";
    private static final String NOME = "Sirnande Lima";
    private static final String TELEFONE = "123456";
    private static final String EMAIL = "teste@teste.com";

    @Test
    @DisplayName("tentar buscar cliente com cpf invalido e gerar erro")
    public void testBuscarClenteCpfInvalido() throws Exception{
        BDDMockito.given(this.clienteService.buscarPorCpf(Mockito.anyString())).willReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.get(URL_BASE + "/cpf/" + CPF).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros").value("Cliente não encontrado para o CPF: "+ CPF));
    }

    @Test
    @DisplayName("Tentar buscar um cliente por cpf")
    public void testBuscarClienteCpfValido() throws Exception{
        BDDMockito.given(this.clienteService.buscarPorCpf(Mockito.anyString()))
                .willReturn(Optional.of(this.obterDadosCliente()));
        mvc.perform(MockMvcRequestBuilders.get(URL_BASE + "/cpf/" + CPF)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(ID))
                .andExpect(jsonPath("$.data.nome", equalTo(NOME)))
                .andExpect(jsonPath("$.data.cpf", equalTo(CPF)))
                .andExpect(jsonPath("$.data.telefone", equalTo(TELEFONE)))
                .andExpect(jsonPath("$.data.email", equalTo(EMAIL)))
                .andExpect(jsonPath("$.erros").isEmpty());
    }

    @Test
    @DisplayName("tentar cadastrar um cliente")
    public void testCadastrarCliente() throws Exception{

        mvc.perform(MockMvcRequestBuilders.post(URL_BASE + "/cadastro-cliente")
                        .content(this.obterJsonRequisicaoPost())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.id").value(ID))
                    .andExpect(jsonPath("$.data.cpf").value(CPF))
                    .andExpect(jsonPath("$.data.nome").value(NOME))
                    .andExpect(jsonPath("$.data.email").value(EMAIL))
                    .andExpect(jsonPath("$.data.telefone").value(TELEFONE))
                    .andExpect(jsonPath("$.erros").isEmpty());
    }

    @Test
    @DisplayName("Retorna erro CPF já existente ao cadastrar um cliente")
    public void testeCadastrarClienteCpfExistente() throws Exception {
        BDDMockito.given(this.clienteService.buscarPorCpf(Mockito.anyString())).willReturn(Optional.of(new Cliente()));

        mvc.perform(MockMvcRequestBuilders.post(URL_BASE + "/cadastro-cliente")
                    .content(this.obterJsonRequisicaoPost())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros").value("CPF já existente."))
                .andExpect(jsonPath("$.data").isEmpty());
    }



    @Test
    @DisplayName("Atualizar cliente dado um detrminado Id")
    public void testAtualizarClientePorId() throws Exception{
        Cliente cliente =  this.obterDadosCliente();
//        BDDMockito.given(this.clienteService.buscarPorId(Mockito.anyLong())).willReturn(Optional.of(new Cliente()));

        when(this.clienteService.buscarPorId(cliente.getId())).thenReturn(Optional.of(cliente));

        mvc.perform(MockMvcRequestBuilders.put(URL_BASE +"/id/1")
                    .content(this.obterJsonRequsicaoPut())
                    .contentType(MediaType.APPLICATION_JSON))
//                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(ID))
                .andExpect(jsonPath("$.data.cpf").value(CPF))
                .andExpect(jsonPath("$.data.nome").value(NOME))
                .andExpect(jsonPath("$.data.email").value("db1@db1.com"))
                .andExpect(jsonPath("$.data.telefone").value(TELEFONE))
                .andExpect(jsonPath("$.erros").isEmpty())
        ;

    }

    @Test
    @DisplayName("Retorna erro ao tentar atualizar um cliente por um id errado")
    public void testAtualizarClientePorIdInexistente() throws Exception {
        Cliente cliente = this.obterDadosCliente();

        when(this.clienteService.buscarPorId(cliente.getId())).thenReturn(Optional.of(cliente));

        mvc.perform(
                MockMvcRequestBuilders.put(URL_BASE + "/id/{id}",3)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.obterJsonRequsicaoPut()))
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.erros").value("Erro cliente não encontrado para o id 3"))
            .andExpect(jsonPath("$.data").isEmpty());


    }

    @Test
    @DisplayName("Deletar um cliente dado um id")
    public void testDeletarClientePorId() throws Exception {
        Cliente cliente = this.obterDadosCliente();

        when(this.clienteService.buscarPorId(cliente.getId())).thenReturn(Optional.of(cliente));

        mvc.perform(MockMvcRequestBuilders.delete(URL_BASE + "/id/{id}", 1))
        .andExpect(status().isOk());
    }


    @Test
    @DisplayName("Retorna erro ao tentar excluir um cliente por um id inexistente")
    public void testDeletarClientePorIdInexistente() throws Exception{
        Cliente cliente = this.obterDadosCliente();

        when(this.clienteService.buscarPorId(cliente.getId())).thenReturn(Optional.of(cliente));

        mvc.perform(MockMvcRequestBuilders.delete(URL_BASE + "/id/{id}", 2))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros").value("Erro ao remover o cliente. Cliente não encontrado para o id 2"))
                .andExpect(jsonPath("$.data").isEmpty());
    }




    private Cliente obterDadosCliente() {
        Cliente cliente = new Cliente();

        cliente.setId(ID);
        cliente.setCpf(CPF);
        cliente.setNome(NOME);
        cliente.setTelefone(TELEFONE);
        cliente.setEmail(EMAIL);

        return cliente;
    }

    private String obterJsonRequisicaoPost() throws JsonProcessingException {
        ClienteDto clienteDto = new ClienteDto();

        clienteDto.setId(ID);
        clienteDto.setCpf(CPF);
        clienteDto.setEmail(EMAIL);
        clienteDto.setNome(NOME);
        clienteDto.setTelefone(TELEFONE);
        ObjectMapper mapper =  new ObjectMapper();
        return mapper.writeValueAsString(clienteDto);
    }

    private String obterJsonRequsicaoPut() throws JsonProcessingException{
        ClienteDto clienteDto = new ClienteDto();

//        clienteDto.setId(ID);
        clienteDto.setTelefone(TELEFONE);
        clienteDto.setNome(NOME);
        clienteDto.setEmail("db1@db1.com");
        clienteDto.setCpf(CPF);

        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(clienteDto);
    }


}
