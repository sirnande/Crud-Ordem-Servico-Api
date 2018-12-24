package com.para.crudos.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.para.crudos.api.dtos.ClienteDTO;
import com.para.crudos.api.exceptions.ValidacaoException;
import com.para.crudos.api.model.Cliente;
import com.para.crudos.api.services.ClienteService;
import com.para.crudos.api.setup.UrlApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.doThrow;
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
    private static final String CPF_INVALIDO = "38244084570";
    private static final String NOME = "Sirnande Lima";
    private static final String TELEFONE = "123456";
    private static final String EMAIL = "teste@teste.com";
    private static final String EMAIL_NOVO = "db1@db1.com";


    @BeforeEach
    public void init(){
        Cliente cliente =  this.obterDadosCliente();
        ClienteDTO clienteDto = this.obterDadosDto(cliente);

        when(this.clienteService.buscarPorId(cliente.getId())).thenReturn(cliente);
        when(this.clienteService.buscarPorCpf(cliente.getCpf())).thenReturn(clienteDto);
        when(this.clienteService.buscarPorEmail(cliente.getEmail())).thenReturn(cliente);
        when(this.clienteService.buscarPorNome(cliente.getNome())).thenReturn(cliente);
        when(this.clienteService.buscarPorTelefone(cliente.getTelefone())).thenReturn(cliente);

    }


    @Test
    @DisplayName("tentar buscar cliente com cpf invalido e gerar erro")
    public void testBuscarClenteCpfInvalido() throws Exception{
        Cliente cliente = this.obterDadosCliente();

        when(this.clienteService.buscarPorCpf(CPF_INVALIDO)).thenThrow(new ValidacaoException("Cliente não encontrado para o CPF: "+ CPF_INVALIDO));

        mvc.perform(MockMvcRequestBuilders.get(URL_BASE + "/cpf/{cpf}", CPF_INVALIDO))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagemDoErroGerado").value("Cliente não encontrado para o CPF: "+ CPF_INVALIDO));
    }

    @Test
    @DisplayName("Tentar buscar um cliente por cpf")
    public void testBuscarClienteCpfValido() throws Exception{

        ClienteDTO clienteDto = this.obterDadosDto(this.obterDadosCliente());
        when(this.clienteService.buscarPorCpf(clienteDto.getCpf())).thenReturn(clienteDto);

        mvc.perform(MockMvcRequestBuilders.get(URL_BASE + "/cpf/{cpf}", CPF))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", equalTo(NOME)))
                .andExpect(jsonPath("$.cpf", equalTo(CPF)))
                .andExpect(jsonPath("$.telefone", equalTo(TELEFONE)))
                .andExpect(jsonPath("$.email", equalTo(EMAIL)));
    }

    @Test
    @DisplayName("tentar cadastrar um cliente")
    public void testCadastrarCliente() throws Exception{

        mvc.perform(MockMvcRequestBuilders.post(URL_BASE + "/cadastro-cliente")
                        .content(this.obterJsonRequisicaoPost())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nome").value(NOME))
                    .andExpect(jsonPath("$.cpf").value(CPF))
                    .andExpect(jsonPath("$.telefone").value(TELEFONE))
                    .andExpect(jsonPath("$.email").value(EMAIL));
    }

    @Test
    @DisplayName("Retorna erro CPF já existente ao cadastrar um cliente")
    public void testCadastrarClienteCpfExistente() throws Exception {


        doThrow(new ValidacaoException("CPF já existe")).when(this.clienteService).validarDadosExistentes(Mockito.any());

        mvc.perform(MockMvcRequestBuilders.post(URL_BASE + "/cadastro-cliente")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.obterJsonRequisicaoPost()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagemDoErroGerado").value("CPF já existe"));
    }



    @Test
    @DisplayName("Atualizar cliente dado um detrminado Id")
    public void testAtualizarClientePorId() throws Exception{
        Cliente cliente =  this.obterDadosCliente();
        ClienteDTO clienteDto = this.obterDadosDto(cliente);
        clienteDto.setEmail(EMAIL_NOVO);

      //  when(this.clienteService.converterCadastroClienteDto(Mockito.any())).thenReturn(clienteDto);

        mvc.perform(MockMvcRequestBuilders.put(URL_BASE +"/id/{id}", 1)
                    .content(this.obterJsonRequsicaoPut())
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.cpf").value(CPF))
                .andExpect(jsonPath("$.nome").value(NOME))
                .andExpect(jsonPath("$.email").value("db1@db1.com"))
                .andExpect(jsonPath("$.telefone").value(TELEFONE));

    }

    @Test
    @DisplayName("Retorna erro ao tentar atualizar um cliente por um id errado")
    public void testAtualizarClientePorIdInexistente() throws Exception {
        Cliente cliente = this.obterDadosCliente();

       // when(this.clienteService.buscarPorId(cliente.getId())).thenReturn(cliente);
        doThrow(new ValidacaoException("Erro cliente não encontrado para o id 3")).when(this.clienteService).buscarPorId(Mockito.anyLong());

        mvc.perform(
                MockMvcRequestBuilders.put(URL_BASE + "/id/{id}",3)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.obterJsonRequsicaoPut()))
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.mensagemDoErroGerado").value("Erro cliente não encontrado para o id 3"));


    }

    @Test
    @DisplayName("Deletar um cliente dado um id")
    public void testDeletarClientePorId() throws Exception {
        Cliente cliente = this.obterDadosCliente();

        when(this.clienteService.buscarPorId(cliente.getId())).thenReturn(cliente);

        mvc.perform(MockMvcRequestBuilders.delete(URL_BASE + "/id/{id}", 1))
        .andExpect(status().isOk());
    }


    @Test
    @DisplayName("Retorna erro ao tentar excluir um cliente por um id inexistente")
    public void testDeletarClientePorIdInexistente() throws Exception{
        Cliente cliente = this.obterDadosCliente();
//
//        when(this.clienteService.buscarPorId(cliente.getId())).thenReturn(cliente);
        doThrow(new ValidacaoException("Erro ao remover o cliente. Cliente não encontrado para o id 2")).when(this.clienteService).buscarPorId(Mockito.anyLong());

        mvc.perform(MockMvcRequestBuilders.delete(URL_BASE + "/id/{id}", 2))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagemDoErroGerado").value("Erro ao remover o cliente. Cliente não encontrado para o id 2"));
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

    private Cliente obterNovosDadosCliente(Cliente cliente, ClienteDTO clienteDto){

        cliente.setId(clienteDto.getId());
        cliente.setNome(clienteDto.getNome());
        cliente.setCpf(clienteDto.getCpf());
        cliente.setEmail(clienteDto.getEmail());
        cliente.setTelefone(clienteDto.getTelefone());

        return cliente;
    }

    private ClienteDTO obterDadosDto(Cliente cliente){
        ClienteDTO clienteDto =  new ClienteDTO();

        clienteDto.setId(cliente.getId());
        clienteDto.setNome(cliente.getNome());
        clienteDto.setCpf(cliente.getCpf());
        clienteDto.setEmail(cliente.getEmail());
        clienteDto.setTelefone(cliente.getTelefone());

        return clienteDto;
    }

    private String obterJsonRequisicaoPost() throws JsonProcessingException {
        ClienteDTO clienteDto = new ClienteDTO();

        clienteDto.setCpf(CPF);
        clienteDto.setEmail(EMAIL);
        clienteDto.setNome(NOME);
        clienteDto.setTelefone(TELEFONE);
        ObjectMapper mapper =  new ObjectMapper();
        return mapper.writeValueAsString(clienteDto);
    }

    private String obterJsonRequsicaoPut() throws JsonProcessingException{
        ClienteDTO clienteDto = new ClienteDTO();

//        clienteDto.setId(ID);
        clienteDto.setTelefone(TELEFONE);
        clienteDto.setNome(NOME);
        clienteDto.setEmail(EMAIL_NOVO);
        clienteDto.setCpf(CPF);

        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(clienteDto);
    }


}
