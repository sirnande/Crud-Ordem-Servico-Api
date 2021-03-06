package com.para.crudos.api.services;

import com.para.crudos.api.dtos.ClienteDTO;
import com.para.crudos.api.model.Cliente;
import com.para.crudos.api.repositories.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.Assert.*;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class ClienteServiceTest {

    @MockBean
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteService clienteService;


    @BeforeEach
    public void setUp() throws Exception{

        BDDMockito.given(this.clienteRepository.save(Mockito.any(Cliente.class))).willReturn(new Cliente());
        BDDMockito.given(this.clienteRepository.findById(Mockito.anyLong())).willReturn(Optional.of(new Cliente()));
        BDDMockito.given(this.clienteRepository.findByCpf(Mockito.anyString())).willReturn(new Cliente());
        BDDMockito.given(this.clienteRepository.findByEmail(Mockito.anyString())).willReturn(new Cliente());
        BDDMockito.given(this.clienteRepository.findByNome(Mockito.anyString())).willReturn(new Cliente());
        BDDMockito.given(this.clienteRepository.findByTelefone(Mockito.anyString())).willReturn(new Cliente());
    }

    @Test
    @DisplayName("Persistindo um cliente na base de dado")
    public void testPersistirCliente(){
        ClienteDTO clienteDTO = this.clienteService.gravar(new ClienteDTO());
        assertNotNull(clienteDTO);
    }

    @Test
    @DisplayName("Tem que retorna um clinete dado um id")
    public void testBuscarClientePorId(){
        Cliente cliente = this.clienteService.buscarPorId(1L);
        assertNotNull(cliente);
    }

    @Test
    @DisplayName("Tem que buscar e retorna um clinete dado um cpf")
    public void testbuscarClientePorCpf(){
        ClienteDTO clienteDTO = this.clienteService.buscarPorCpf("123456789");
        assertTrue(clienteDTO!=null);
    }

    @Test
    @DisplayName("Tem que buscar e retorna um cliente dado um email")
    public void buscarClientePorEmail() {
        Cliente cliente = this.clienteService.buscarPorEmail("teste@teste.com");
        assertTrue(cliente != null);
    }

    @Test
    @DisplayName("Tem que buscar e rtorna um cliente dado u telefone")
    public void buscarClientePorTelefone(){
        Cliente cliente = this.clienteService.buscarPorTelefone("(99) 9999-9999");
        assertTrue(cliente  != null);
    }

    @Test
    @DisplayName("Te que buscar e retorna um clinete dado um nome")
    public void buscarClientePorNome(){
        Cliente cliente = this.clienteService.buscarPorNome("teste");
        assertTrue(cliente  != null);
    }

}
