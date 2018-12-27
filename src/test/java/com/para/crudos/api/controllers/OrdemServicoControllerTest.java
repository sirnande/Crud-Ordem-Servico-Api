package com.para.crudos.api.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.para.crudos.api.dtos.OrdemServicoDTO;
import com.para.crudos.api.dtos.TecnicoDTO;
import com.para.crudos.api.enums.Status;
import com.para.crudos.api.model.Cliente;
import com.para.crudos.api.model.Endereco;
import com.para.crudos.api.model.OrdemServico;
import com.para.crudos.api.model.Tecnico;
import com.para.crudos.api.repositories.ClienteRepository;
import com.para.crudos.api.repositories.OrdemServicoRepository;
import com.para.crudos.api.services.ClienteService;
import com.para.crudos.api.services.EnderecoService;
import com.para.crudos.api.services.OrdemServicoService;
import com.para.crudos.api.services.TecnicoService;
import com.para.crudos.api.setup.UrlApi;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.convert.ConversionService;
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
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class OrdemServicoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Mock
    private OrdemServicoService ordemServicoService;

    @MockBean
    private OrdemServicoRepository ordemServicoRepository;

    @Mock
    private ClienteService clienteService;

    @MockBean
    private ClienteRepository clienteRepository;

    @MockBean
    private EnderecoService enderecoService;

    @MockBean
    private TecnicoService tecnicoService;

    @Autowired
    private ConversionService conversionService;

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
        OrdemServicoDTO ordemServicoDTO = this.conversionService.convert(ordemServico, OrdemServicoDTO.class);
        OrdemServicoDTO ordemServicoDTOJson = obterDadosDTO();
        ordemServicoDTOJson.setId(null);
        Cliente cliente = obterDadosCliente(ordemServicoDTO);
        when(this.clienteService.buscarPorId(ordemServicoDTOJson.getCliente())).thenReturn(obterDadosCliente(ordemServicoDTO));
        when(this.clienteRepository.findById(ordemServicoDTO.getCliente())).thenReturn(Optional.ofNullable(cliente));
        when(this.enderecoService.buscarPorId(ordemServicoDTOJson.getEndereco())).thenReturn(obterDadosEndereco(ordemServicoDTO));
        when(this.tecnicoService.buscarPorId(ordemServicoDTOJson.getTecnico())).thenReturn(obterDadosTecnicoDTO(ordemServicoDTO));

        when(this.ordemServicoService.gravar(ordemServicoDTOJson)).thenReturn(ordemServicoDTO);
        when(this.ordemServicoRepository.save(Mockito.any())).thenReturn(ordemServico);

        mvc.perform(MockMvcRequestBuilders.post(URL_BASE + "/cadastro-os")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.obterJsonRequisitPost()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.endereco").value(ID_ENDERECO))
                .andExpect(jsonPath("$.cliente").value(ID_CLIENTE))
                .andExpect(jsonPath("$.tecnico").value(ID_TECNICO));
    }


    @Test
    @DisplayName("Buscar ordem de servico por id")
    public void testBuscarOrdemServicoPorId() throws Exception{
        OrdemServico ordemServico = obterDadosOrdemServico();
        OrdemServicoDTO ordemServicoDTO = this.conversionService.convert(ordemServico, OrdemServicoDTO.class);

        when(this.ordemServicoService.buscarPorId(ordemServicoDTO.getId())).thenReturn(ordemServicoDTO);
        when(this.ordemServicoRepository.findById(ordemServicoDTO.getId())).thenReturn(Optional.ofNullable(ordemServico));

        mvc.perform(MockMvcRequestBuilders.get(URL_BASE + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.cliente").value(ID_CLIENTE))
                .andExpect(jsonPath("$.endereco").value(ID_ENDERECO))
                .andExpect(jsonPath("$.tecnico").value(ID_TECNICO));
    }

    @Test
    @DisplayName("Buscar ordem de servico por id inexistente")
    public void testBuscarOrdemServicoPorIdInexistente() throws Exception{
        OrdemServico ordemServico = this.obterDadosOrdemServico();

        //when(this.ordemServicoService.buscarPorId(ordemServico.getId())).thenReturn(Optional.of(ordemServico));
        when(this.ordemServicoRepository.findById(ordemServico.getId())).thenReturn(Optional.ofNullable(ordemServico));

        mvc.perform(MockMvcRequestBuilders.get(URL_BASE + "/{id}", 2))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagemDoErroGerado").value("Ordem de Servico não encontrada para o ID: 2"));
    }



    @Test
    @DisplayName("Buscar ordem de servico por id do cliente")
    public void testBuscarOrdemServicoPorIdCliente() throws Exception{
        List<OrdemServicoDTO> ordemServicosDTO = this.obterListaOrdemServicoDTO();
        Page<OrdemServicoDTO> ordemServicosDTOPage = new PageImpl(ordemServicosDTO);

        List<OrdemServico> ordemServicos = this.obterListaOrdemServico();
        Page<OrdemServico> ordemServicoPage = new PageImpl(ordemServicos);

        Cliente cliente = obterDadosCliente(ordemServicosDTO.get(0));
        when(this.clienteService.buscarPorId(cliente.getId())).thenReturn(cliente);
        when(this.clienteRepository.findById(cliente.getId())).thenReturn(Optional.ofNullable(cliente));


        when(this.ordemServicoService.buscarPorClienteId(1L, 0, 2,"id", "DESC")).thenReturn(ordemServicosDTOPage);
        when(this.ordemServicoRepository.findByClienteId(1L, PageRequest.of(0, 2, Sort.Direction.DESC, "id"))).thenReturn(ordemServicoPage);
        List<OrdemServico> ordemServicoDTOList = ordemServicoPage.get().collect(Collectors.toList());


        mvc.perform(MockMvcRequestBuilders.get(URL_BASE + "/cliente/{clienteId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.numberOfElements").value(1));
        Assert.assertEquals(1L,ordemServicoDTOList.get(0).getCliente().getId().longValue());

    }


    @Test
    @DisplayName("Buscar ordem de servico por id cliente inexistente")
    public void testBuscarOrdemServicoPorIdClienteInexistente() throws Exception{
        List<OrdemServicoDTO> ordemServicosDTO = this.obterListaOrdemServicoDTO();
        Page<OrdemServicoDTO> ordemServicosDTOPage = new PageImpl(ordemServicosDTO);

        List<OrdemServico> ordemServicos = this.obterListaOrdemServico();
        Page<OrdemServico> ordemServicoPage = new PageImpl(ordemServicos);

        Cliente cliente = obterDadosCliente(ordemServicosDTO.get(0));

        when(this.ordemServicoService.buscarPorClienteId(1L, 0, 2,"id", "DESC")).thenReturn(ordemServicosDTOPage);
        when(this.clienteService.buscarPorId(ordemServicosDTO.get(0).getCliente())).thenReturn(cliente);
        when(this.clienteRepository.findById(ordemServicosDTO.get(0).getCliente())).thenReturn(Optional.ofNullable(cliente));
        when(this.ordemServicoRepository.findByClienteId(1L, PageRequest.of(0, 2, Sort.Direction.DESC, "id"))).thenReturn(ordemServicoPage);


        mvc.perform(MockMvcRequestBuilders.get(URL_BASE + "/cliente/{clienteId}", 2))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagemDoErroGerado").value("Cliente não encontrado para o ID: 2"));
    }


    @Test
    @DisplayName("Atualizar ordem de servico por id")
    public void testAtualizarOrdemServicoPorId() throws Exception{
        OrdemServico ordemServico =  this.obterDadosOrdemServico();
        OrdemServicoDTO ordemServicoDTO = this.conversionService.convert(ordemServico, OrdemServicoDTO.class);
        Date date = new Date();
        ordemServicoDTO.setDataFinalizada(this.simpleDateFormat.format(date));

        String data = ordemServicoDTO.getDataFinalizada();

        ordemServicoDTO.setStatus(Status.CONCLUIDO.name());

        Cliente cliente = obterDadosCliente(ordemServicoDTO);
        Endereco endereco = obterDadosEndereco(ordemServicoDTO);
        TecnicoDTO tecnicoDTO = obterDadosTecnicoDTO(ordemServicoDTO);

        when(this.clienteService.buscarPorId(ordemServicoDTO.getCliente())).thenReturn(cliente);
        when(this.clienteRepository.findById(cliente.getId())).thenReturn(Optional.ofNullable(cliente));
        when(this.enderecoService.buscarPorId(ordemServicoDTO.getEndereco())).thenReturn(endereco);
        when(this.tecnicoService.buscarPorId(ordemServicoDTO.getTecnico())).thenReturn(tecnicoDTO);

        when(this.ordemServicoService.atualizar(ordemServicoDTO)).thenReturn(ordemServicoDTO);
        when(this.ordemServicoService.buscarPorId(ordemServicoDTO.getId())).thenReturn(this.conversionService.convert(ordemServico, OrdemServicoDTO.class));
        when(this.ordemServicoRepository.findById(ordemServicoDTO.getId())).thenReturn(Optional.ofNullable(ordemServico));

        mvc.perform(MockMvcRequestBuilders.put(URL_BASE + "/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.obterJsonRequisitPut(date)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(Status.CONCLUIDO.name()))
                .andExpect(jsonPath("$.dataFinalizada").value(data))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Atualizar ordem de servico por id invalido")
    public void testAtualizarOrdemServicoPorIdInvalido() throws Exception{
        OrdemServico ordemServico =  this.obterDadosOrdemServico();
        OrdemServicoDTO ordemServicoDTO = this.conversionService.convert(ordemServico, OrdemServicoDTO.class);

        when(this.ordemServicoService.buscarPorId(ordemServico.getId())).thenReturn(ordemServicoDTO);
        when(this.ordemServicoRepository.findById(ordemServico.getId())).thenReturn(Optional.ofNullable(ordemServico));

        mvc.perform(MockMvcRequestBuilders.put(URL_BASE + "/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.obterJsonRequisitPut(new Date())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagemDoErroGerado").value("Ordem de Servico não encontrada para o ID: 2"));
    }


    @Test
    @DisplayName("Excluir uma ordem de servico por id")
    public void testExcluirOrdemServicoPorId() throws Exception{
        OrdemServico ordemServico = this.obterDadosOrdemServico();
        OrdemServicoDTO ordemServicoDTO = this.conversionService.convert(ordemServico, OrdemServicoDTO.class);

        when(this.ordemServicoService.buscarPorId(ordemServico.getId())).thenReturn(ordemServicoDTO);
        when(this.ordemServicoRepository.findById(ordemServicoDTO.getId())).thenReturn(Optional.ofNullable(ordemServico));

        mvc.perform(MockMvcRequestBuilders.delete(URL_BASE + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Ordem de servico excluido com sucesso..."));
    }

    @Test
    @DisplayName("Excluir uma ordem de servico por id invalido")
    public void testExcluirOrdemServicoPorIdInvalido() throws Exception{
        OrdemServico ordemServico = this.obterDadosOrdemServico();
        OrdemServicoDTO ordemServicoDTO = this.conversionService.convert(ordemServico, OrdemServicoDTO.class);

        when(this.ordemServicoService.buscarPorId(ordemServico.getId())).thenReturn(ordemServicoDTO);
        when(this.ordemServicoRepository.findById(ordemServicoDTO.getId())).thenReturn(Optional.ofNullable(ordemServico));

        mvc.perform(MockMvcRequestBuilders.delete(URL_BASE + "/{id}", 2))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagemDoErroGerado").value("Ordem de Servico não encontrada para o ID: 2"));
    }

    private Cliente obterDadosCliente(OrdemServicoDTO ordemServicoDTO){
        Cliente cliente = new Cliente();
        cliente.setId(ordemServicoDTO.getCliente());

        return cliente;
    }

    private Endereco obterDadosEndereco(OrdemServicoDTO ordemServicoDTO){
        Endereco endereco = new Endereco();
        endereco.setId(ordemServicoDTO.getEndereco());

        return endereco;
    }

    private Tecnico obterDadosTecnico(OrdemServicoDTO ordemServicoDTO){
        Tecnico tecnico = new Tecnico();
        tecnico.setId(ordemServicoDTO.getTecnico());

        return tecnico;
    }

    private TecnicoDTO obterDadosTecnicoDTO(OrdemServicoDTO ordemServicoDTO){
        TecnicoDTO tecnico = new TecnicoDTO();
        tecnico.setId(ordemServicoDTO.getTecnico());

        return tecnico;
    }

    private OrdemServicoDTO obterDadosDTO(){
        OrdemServicoDTO ordemServicoDto = new OrdemServicoDTO();

        ordemServicoDto.setEspecificacao(ESPECIFICACAO);
        ordemServicoDto.setStatus(STATUS);
        ordemServicoDto.setDataAberta(this.simpleDateFormat.format(DATAABERTA));
        ordemServicoDto.setCliente(ID_CLIENTE);
        ordemServicoDto.setEndereco(ID_ENDERECO);
        ordemServicoDto.setTecnico(ID_TECNICO);

        return ordemServicoDto;
    }


    private String obterJsonRequisitPost() throws JsonProcessingException{
        OrdemServicoDTO ordemServicoDto = new OrdemServicoDTO();

        ordemServicoDto.setEspecificacao(ESPECIFICACAO);
        ordemServicoDto.setStatus(STATUS);
        ordemServicoDto.setDataAberta(this.simpleDateFormat.format(DATAABERTA));
        ordemServicoDto.setCliente(ID_CLIENTE);
        ordemServicoDto.setEndereco(ID_ENDERECO);
        ordemServicoDto.setTecnico(ID_TECNICO);

        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(ordemServicoDto);
    }

    private String obterJsonRequisitPut(Date date) throws JsonProcessingException{
        OrdemServicoDTO ordemServicoDto = new OrdemServicoDTO();

        ordemServicoDto.setEspecificacao(ESPECIFICACAO);
        ordemServicoDto.setStatus("CONCLUIDO");
        ordemServicoDto.setDataAberta(this.simpleDateFormat.format(DATAABERTA));
        ordemServicoDto.setDataFinalizada(this.simpleDateFormat.format(date));
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

        ordemServico.setCliente(new Cliente());
        ordemServico.getCliente().setId(ID_CLIENTE);

        ordemServico.setEndereco(new Endereco());
        ordemServico.getEndereco().setId(ID_ENDERECO);

        ordemServico.setTecnico(new Tecnico());
        ordemServico.getTecnico().setId(ID_TECNICO);

        return ordemServico;
    }



    private List<OrdemServicoDTO> obterListaOrdemServicoDTO(){
        List<OrdemServicoDTO> ordemServicos = new ArrayList<>();
        ordemServicos.add(this.conversionService.convert(this.obterDadosOrdemServico(), OrdemServicoDTO.class));

        return ordemServicos;
    }

    private List<OrdemServico> obterListaOrdemServico(){
        List<OrdemServico> ordemServicos = new ArrayList<>();
        ordemServicos.add(this.obterDadosOrdemServico());

        return ordemServicos;
    }

}
