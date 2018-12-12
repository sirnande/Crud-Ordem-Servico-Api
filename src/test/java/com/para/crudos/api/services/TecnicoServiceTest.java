package com.para.crudos.api.services;


import com.para.crudos.api.model.Tecnico;
import com.para.crudos.api.repositories.TecnicoRepository;
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class TecnicoServiceTest {

    @MockBean
    private TecnicoRepository tecnicoRepository;

    @Autowired
    private TecnicoService tecnicoService;


    @BeforeEach
    public void setUp() throws Exception{
        BDDMockito.given(this.tecnicoRepository.save(Mockito.any(Tecnico.class))).willReturn(new Tecnico());
        BDDMockito.given(this.tecnicoRepository.findById(Mockito.anyLong())).willReturn(Optional.of(new Tecnico()));
        BDDMockito.given(this.tecnicoRepository.findByNome(Mockito.anyString())).willReturn(new Tecnico());
    }

    @Test
    public void testPersistirTecnico(){
        Tecnico tecnico = this.tecnicoService.persistir(new Tecnico());
        assertNotNull(tecnico);
    }

    @Test
    public void testbuscarPorId(){
        Optional<Tecnico> tecnico = this.tecnicoService.buscarPorId(1L);
        assertTrue(tecnico.isPresent());
    }

    @Test
    public void testBuscarPorNome(){
        Optional<Tecnico> tecnico = this.tecnicoService.buscarPorNome("teste");
        assertTrue(tecnico.isPresent());
    }

}
