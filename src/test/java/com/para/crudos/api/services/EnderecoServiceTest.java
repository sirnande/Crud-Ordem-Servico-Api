package com.para.crudos.api.services;

import com.para.crudos.api.model.Endereco;
import com.para.crudos.api.repositories.EnderecoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class EnderecoServiceTest {

    @MockBean
    private EnderecoRepository enderecoRepository;


    @Autowired
    private EnderecoService enderecoService;

    @BeforeEach
    public void setUp() throws Exception{
        BDDMockito.given(this.enderecoRepository.save(Mockito.any(Endereco.class))).willReturn(new Endereco());
        BDDMockito.given(this.enderecoRepository.findByCep(Mockito.anyString())).willReturn(new Endereco());
        BDDMockito.given(this.enderecoRepository.findByRua(Mockito.anyString())).willReturn(Mockito.anyList());
//        BDDMockito.given(this.enderecoRepository.findByCidade(Mockito.anyString())).willReturn();
    }

    @Test
    public void testPersistir(){
        Endereco endereco = this.enderecoService.persistir(new Endereco());
        assertNotNull(endereco);
    }

    @Test
    public void testBuscarPorCep(){
        Optional<Endereco> endereco = this.enderecoService.buscarPorCep("123");
        assertTrue(endereco.isPresent());
    }

    @Test
    public void testBuscarPorRua(){
        Optional<List<Endereco>> enderecos = this.enderecoService.buscarPorRua("teste");
        assertTrue(enderecos.isPresent());
    }

    @Test
    public void testBuscarPorCidade(){
        Optional<List<Endereco>> enderecos = this.enderecoService.buscarPorCidade("Maringá");
        assertTrue(enderecos.isPresent());
    }

}
