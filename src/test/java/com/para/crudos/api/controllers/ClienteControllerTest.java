package com.para.crudos.api.controllers;

import com.para.crudos.api.model.Cliente;
import com.para.crudos.api.services.ClienteService;
import com.para.crudos.api.setup.UrlApi;
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

    private static final String BUSCAR_CLIENTE_URL = UrlApi.URL+"/clientes/cpf/";
    private static final Long ID = Long.valueOf(1);
    private static final String CPF = "01834275210";
    private static final String NOME = "Sirnande Lima";
    private static final String TELEFONE = "123456";
    private static final String EMAIL = "teste@teste.com";

    @Test
    public void testBuscarClenteCpfInvalido() throws Exception{
        BDDMockito.given(this.clienteService.buscarPorCpf(Mockito.anyString())).willReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.get(BUSCAR_CLIENTE_URL + CPF).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value("Cliente não encontrado para o CPF "+ CPF));
    }

    @Test
    public void testBuscarClienteCpfValido() throws Exception{
        BDDMockito.given(this.clienteService.buscarPorCpf(Mockito.anyString()))
                .willReturn(Optional.of(this.obterDadosCliente()));
        mvc.perform(MockMvcRequestBuilders.get(BUSCAR_CLIENTE_URL + CPF)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(ID))
                .andExpect(jsonPath("$.data.nome", equalTo(NOME)))
                .andExpect(jsonPath("$.data.cpf", equalTo(CPF)))
                .andExpect(jsonPath("$.data.telefone", equalTo(TELEFONE)))
                .andExpect(jsonPath("$.data.email", equalTo(EMAIL)))
                .andExpect(jsonPath("$.errors").isEmpty());
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


}
