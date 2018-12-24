package com.para.crudos.api.services;


import com.para.crudos.api.dtos.TecnicoDTO;
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
    public void testSalvarTecnico(){
        TecnicoDTO tecnicoDTO = this.tecnicoService.salvar(new TecnicoDTO());
        assertNotNull(tecnicoDTO);
    }

    @Test
    public void testbuscarPorId(){
        TecnicoDTO tecnicoDTO = this.tecnicoService.buscarPorId(1L);
        assertTrue(tecnicoDTO != null);
    }

    @Test
    public void testBuscarPorNome(){
        TecnicoDTO tecnicoDTO = this.tecnicoService.buscarPorNome("teste");
        assertTrue(tecnicoDTO != null);
    }

}
