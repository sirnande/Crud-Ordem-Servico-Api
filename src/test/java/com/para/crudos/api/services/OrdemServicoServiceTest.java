package com.para.crudos.api.services;


import com.para.crudos.api.model.OrdemServico;
import com.para.crudos.api.repositories.OrdemServicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class OrdemServicoServiceTest {

    @MockBean
    private OrdemServicoRepository ordemServicoRepository;

    @Autowired
    private OrdemServicoService ordemServicoService;

    @BeforeEach
    public void setUp() throws Exception{
        BDDMockito
                .given(this.ordemServicoRepository.findByClienteId(Mockito.anyLong(), Mockito.any(PageRequest.class)))
                .willReturn(new PageImpl<>(new ArrayList<>()));
        BDDMockito.given(this.ordemServicoRepository.findById(Mockito.anyLong())).willReturn(Optional.of(new OrdemServico()));
        BDDMockito.given(this.ordemServicoRepository.save(Mockito.any(OrdemServico.class))).willReturn(new OrdemServico());
    }

    @Test
    public void testBucarOrdemServicoPorclienteId(){

        Page<OrdemServico> ordemServicos = this.ordemServicoService.buscarPorClienteId(1L, new PageRequest(0,10));
        assertNotNull(ordemServicos);

    }

    @Test
    public void testBuscarOrdemServicoPorId(){
        Optional<OrdemServico> ordemServico = this.ordemServicoService.buscarPorId(1L);

        assertTrue(ordemServico.isPresent());
    }

    @Test
    public void testPersistirOrdemServico(){

        OrdemServico ordemServico = this.ordemServicoService.persistir(new OrdemServico());

        assertNotNull(ordemServico);
    }


}
