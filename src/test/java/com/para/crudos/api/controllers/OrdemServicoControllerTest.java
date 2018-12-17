package com.para.crudos.api.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.para.crudos.api.dtos.OrdemServicoDto;
import com.para.crudos.api.enums.Status;
import com.para.crudos.api.model.Cliente;
import com.para.crudos.api.model.Endereco;
import com.para.crudos.api.model.OrdemServico;
import com.para.crudos.api.model.Tecnico;
import com.para.crudos.api.services.ClienteService;
import com.para.crudos.api.services.EnderecoService;
import com.para.crudos.api.services.OrdemServicoService;
import com.para.crudos.api.services.TecnicoService;
import com.para.crudos.api.setup.UrlApi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class OrdemServicoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrdemServicoService ordemServicoService;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private EnderecoService enderecoService;

    @MockBean
    private TecnicoService tecnicoService;

    private static final String URL_BASE = UrlApi.URL + "/os";
    private static final Long ID =  1l;
    private static final String ESPECIFICACAO = "Concertar vazamento de tubulação";
    private static final Date DATAABERTA = new Date();
    private static final Date DATAFINALIZADA = null;
    private static final String STATUS = Status.PENDENTE.name();
    private static final Long ID_CLIENTE = 1L;
    private static final Long ID_TECNICO = 1L;
    private static final Long ID_ENDERECO = 1L;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Test
    @DisplayName("Cadastrar uma nova ordem de servico")
    public void tetsCadastrarOrdemServico() throws Exception{
        OrdemServico ordemServico = this.obterDadosOrdemServico();

        when(this.clienteService.buscarPorId(Mockito.anyLong())).thenReturn(Optional.of(new Cliente()));
        when(this.enderecoService.buscarPorId(Mockito.anyLong())).thenReturn(Optional.of(new Endereco()));
        when(this.tecnicoService.buscarPorId(Mockito.anyLong())).thenReturn(Optional.of(new Tecnico()));

        when(this.ordemServicoService.persistir(Mockito.any(OrdemServico.class))).thenReturn(ordemServico);

        mvc.perform(MockMvcRequestBuilders.post(URL_BASE + "/cadastro-os")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.obterJsonRequisitPost()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.cliente").value(ID_CLIENTE))
                .andExpect(jsonPath("$.erros").isEmpty());
    }


    @Test
    @DisplayName("Buscar ordem de servico por id")
    public void testBuscarOrdemServicoPorId() throws Exception{
        OrdemServico ordemServico = this.obterDadosOrdemServico();

        when(this.ordemServicoService.buscarPorId(ordemServico.getId())).thenReturn(Optional.of(ordemServico));

        mvc.perform(MockMvcRequestBuilders.get(URL_BASE + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(ID))
                .andExpect(jsonPath("$.data.cliente").value(ID_CLIENTE))
                .andExpect(jsonPath("$.data.endereco").value(ID_ENDERECO))
                .andExpect(jsonPath("$.data.tecnico").value(ID_TECNICO))
                .andExpect(jsonPath("$.erros").isEmpty());
    }

    @Test
    @DisplayName("Buscar ordem de servico por id inexistente")
    public void testBuscarOrdemServicoPorIdInexistente() throws Exception{
        OrdemServico ordemServico = this.obterDadosOrdemServico();

        when(this.ordemServicoService.buscarPorId(ordemServico.getId())).thenReturn(Optional.of(ordemServico));

        mvc.perform(MockMvcRequestBuilders.get(URL_BASE + "/{id}", 2))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros").value("Ordem de servico não encontardo pelo ID: 2"))
                .andExpect(jsonPath("$.data").isEmpty());
    }



    @Test
    @DisplayName("Buscar ordem de servico por id do cliente")
    public void testBuscarOrdemServicoPorIdCliente() throws Exception{
        List<OrdemServico> ordemServicos = this.obterListaOrdemServico();
        Page<OrdemServico> ordemServicosPage = new PageImpl(ordemServicos);
        when(this.ordemServicoService.buscarPorClienteId(1L, PageRequest.of(0, 2, Sort.Direction.DESC, "id"))).thenReturn(ordemServicosPage);

        mvc.perform(MockMvcRequestBuilders.get(URL_BASE + "/cliente/{clienteId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.number").value(0))
                .andExpect(jsonPath("$.data.numberOfElements").value(1))
                .andExpect(jsonPath("$.erros").isEmpty());
    }

    @Test
    @DisplayName("Buscar ordem de servico por id cliente inexistente")
    public void testBuscarOrdemServicoPorIdClienteInexistente() throws Exception{
        List<OrdemServico> ordemServicos =  this.obterListaOrdemServico();
        Page<OrdemServico> ordemServicoPage = new PageImpl(ordemServicos);
        when(this.ordemServicoService.buscarPorClienteId(1L, PageRequest.of(0, 2, Sort.Direction.DESC, "id"))).thenReturn(ordemServicoPage);

        mvc.perform(MockMvcRequestBuilders.get(URL_BASE + "/cliente/{clienteId}", 2))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros").value("Erro cliente não encontrado para o id: 2"))
                .andExpect(jsonPath("$.data").isEmpty());
    }


    @Test
    @DisplayName("Atualizar ordem de servico por id")
    public void testAtualizarOrdemServicoPorId() throws Exception{
        OrdemServico ordemServico =  this.obterDadosOrdemServico();
        when(this.clienteService.buscarPorId(Mockito.anyLong())).thenReturn(Optional.of(new Cliente()));
        when(this.enderecoService.buscarPorId(Mockito.anyLong())).thenReturn(Optional.of(new Endereco()));
        when(this.tecnicoService.buscarPorId(Mockito.anyLong())).thenReturn(Optional.of(new Tecnico()));

        when(this.ordemServicoService.buscarPorId(ordemServico.getId())).thenReturn(Optional.of(ordemServico));

        mvc.perform(MockMvcRequestBuilders.put(URL_BASE + "/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.obterJsonRequisitPut()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value(Status.CONCLUIDO.name()))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.erros").isEmpty());
    }

    @Test
    @DisplayName("Atualizar ordem de servico por id invalido")
    public void testAtualizarOrdemServicoPorIdInvalido() throws Exception{
        OrdemServico ordemServico =  this.obterDadosOrdemServico();
        when(this.clienteService.buscarPorId(Mockito.anyLong())).thenReturn(Optional.of(new Cliente()));
        when(this.enderecoService.buscarPorId(Mockito.anyLong())).thenReturn(Optional.of(new Endereco()));
        when(this.tecnicoService.buscarPorId(Mockito.anyLong())).thenReturn(Optional.of(new Tecnico()));

        when(this.ordemServicoService.buscarPorId(ordemServico.getId())).thenReturn(Optional.of(ordemServico));

        mvc.perform(MockMvcRequestBuilders.put(URL_BASE + "/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.obterJsonRequisitPut()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros").value("Error Ordem de Servico não encontrada para o id 2"))
                .andExpect(jsonPath("$.data").isEmpty());
    }


    @Test
    @DisplayName("Excluir uma ordem de servico por id")
    public void testExcluirOrdemServicoPorId() throws Exception{
        OrdemServico ordemServico = this.obterDadosOrdemServico();

        when(this.ordemServicoService.buscarPorId(ordemServico.getId())).thenReturn(Optional.of(ordemServico));

        mvc.perform(MockMvcRequestBuilders.delete(URL_BASE + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.erros").isEmpty());
    }

    @Test
    @DisplayName("Excluir uma ordem de servico por id invalido")
    public void testExcluirOrdemServicoPorIdInvalido() throws Exception{
        OrdemServico ordemServico = this.obterDadosOrdemServico();

        when(this.ordemServicoService.buscarPorId(ordemServico.getId())).thenReturn(Optional.of(ordemServico));

        mvc.perform(MockMvcRequestBuilders.delete(URL_BASE + "/{id}", 2))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros").value("Erro ao remover ordem de serviço. Registro não encotrado para o id 2"))
                .andExpect(jsonPath("$.data").isEmpty());
    }




    private String obterJsonRequisitPost() throws JsonProcessingException{
        OrdemServicoDto ordemServicoDto = new OrdemServicoDto();

        ordemServicoDto.setEspecificacao(ESPECIFICACAO);
        ordemServicoDto.setStatus(STATUS);
        ordemServicoDto.setDataAberta(this.simpleDateFormat.format(DATAABERTA));
        ordemServicoDto.setCliente(ID_CLIENTE);
        ordemServicoDto.setEndereco(ID_ENDERECO);
        ordemServicoDto.setTecnico(ID_TECNICO);

        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(ordemServicoDto);
    }

    private String obterJsonRequisitPut() throws JsonProcessingException{
        OrdemServicoDto ordemServicoDto = new OrdemServicoDto();

        ordemServicoDto.setEspecificacao(ESPECIFICACAO);
        ordemServicoDto.setStatus("CONCLUIDO");
        ordemServicoDto.setDataAberta(this.simpleDateFormat.format(DATAABERTA));
        ordemServicoDto.setDataFinalizada(this.simpleDateFormat.format(new Date()));
        ordemServicoDto.setCliente(ID_CLIENTE);
        ordemServicoDto.setEndereco(ID_ENDERECO);
        ordemServicoDto.setTecnico(ID_TECNICO);

        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(ordemServicoDto);
    }


    private OrdemServico obterDadosOrdemServico(){
        OrdemServico ordemServico = new OrdemServico();

        ordemServico.setId(ID);
        ordemServico.setStatus(Status.valueOf(STATUS));
        ordemServico.setEspecificacao(ESPECIFICACAO);
        ordemServico.setDataAberta(DATAABERTA);
        ordemServico.setDataFinalizada(DATAFINALIZADA);

        ordemServico.setCliente(new Cliente());
        ordemServico.getCliente().setId(ID_CLIENTE);

        ordemServico.setEndereco(new Endereco());
        ordemServico.getEndereco().setId(ID_ENDERECO);

        ordemServico.setTecnico(new Tecnico());
        ordemServico.getTecnico().setId(ID_TECNICO);

        return ordemServico;
    }

    private List<OrdemServico> obterListaOrdemServico(){
        List<OrdemServico> ordemServicos = new ArrayList<>();
        ordemServicos.add(this.obterDadosOrdemServico());

        return ordemServicos;
    }
















}
