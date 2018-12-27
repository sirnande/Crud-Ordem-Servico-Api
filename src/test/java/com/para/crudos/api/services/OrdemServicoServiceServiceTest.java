package com.para.crudos.api.services;


import com.para.crudos.api.dtos.OrdemServicoDTO;
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
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class OrdemServicoServiceServiceTest {

    @MockBean
    private OrdemServicoRepository ordemServicoRepository;

    @Autowired
    private OrdemServicoService ordemServicoService;

    @Autowired
    private ConversionService conversionService;

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
        PageRequest pageRequest =   PageRequest.of(1, 1, Sort.Direction.valueOf("DESC"), "id");

        Page<OrdemServico> ordemServicos = this.ordemServicoRepository.findByClienteId(1L, pageRequest);
        assertNotNull(ordemServicos);

    }

    @Test
    public void testBuscarOrdemServicoPorId(){
        Optional<OrdemServico> ordemServico = this.ordemServicoRepository.findById(1L);

        assertNotNull(ordemServico);
    }

    @Test
    public void testPersistirOrdemServico(){


        OrdemServico ordemServico = this.ordemServicoRepository.save(new OrdemServico());

        assertNotNull(ordemServico);
    }


}
