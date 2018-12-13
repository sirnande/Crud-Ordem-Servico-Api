package com.para.crudos.api.controllers;

import com.para.crudos.api.auditoria.Auditoria;
import com.para.crudos.api.dtos.ClienteDto;
import com.para.crudos.api.model.Cliente;
import com.para.crudos.api.response.Response;
import com.para.crudos.api.services.ClienteService;
import com.para.crudos.api.setup.UrlApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@RestController
@RequestMapping(UrlApi.URL+"/clientes")
public class ClienteController {
    private static final Logger log = LoggerFactory.getLogger(ClienteController.class);


    @Autowired
    private ClienteService clienteService;

    private Auditoria<ClienteDto> auditoria =  new Auditoria<>();

    /**
     *
     * Método responsável por cadastrar um novo cliente na base de dados
     *
     * @param clienteDto
     * @param result
     * @return ResponseEntity<Response<ClienteDto>>
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/cadastro-cliente")
    public ResponseEntity<Response<ClienteDto>> cadastrar(@Valid @RequestBody ClienteDto clienteDto,
                                                          BindingResult result) throws NoSuchAlgorithmException{
        log.info("Cadastrando cliente: {}", clienteDto.toString());
        Response<ClienteDto> response = new Response<>();

        validarDadosExistentes(clienteDto, result);
        Cliente cliente = this.converterDtoParaCliente(clienteDto, result);

        if(result.hasErrors()){
            log.error("Erro validando dados de cadastro Cliente: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        auditoria.post(new ClienteDto(), clienteDto, "cliente");
        this.clienteService.persistir(cliente);
        response.setData(this.converterCadastroClienteDto(cliente));
        return ResponseEntity.ok(response);
    }





    /**
     * Método responsável por retorna um cliente dado um CPF
     *
     * @param cpf
     * @return ResponseEntity<Response<ClienteDto>>
     */
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Response<ClienteDto>> buscarPorCpf(@PathVariable("cpf") String cpf){
        log.info("Buscando clinete por cpf: {}", cpf);
        Response<ClienteDto> response = new Response<>();
        Optional<Cliente> cliente = this.clienteService.buscarPorCpf(cpf);

        if(!cliente.isPresent()){
            log.info("Cliente não encontrado para o CPF: {}", cpf);
            response.getErros().add("Cliente não encontrado para o CPF: " +cpf);
            return ResponseEntity.badRequest().body(response);
        }

        response.setData(this.converterCadastroClienteDto(cliente.get()));
        return ResponseEntity.ok(response);
    }


    /**
     * Método responsável por atualizar um cliente dado um id de lçocalização
     *
     * @param id
     * @param clienteDto
     * @param result
     * @return ResponseEntity<Response<ClienteDto>>
     * @throws NoSuchAlgorithmException
     */
    @PutMapping("/id/{id}")
    public ResponseEntity<Response<ClienteDto>> atualizar(@PathVariable("id") Long id,
                                                          @Valid @RequestBody ClienteDto clienteDto,  BindingResult result) throws NoSuchAlgorithmException{
        log.info("Atualizar cliente: {}", clienteDto.toString());

        Response<ClienteDto> response = new Response<>();

        Optional<Cliente> cliente =  this.clienteService.buscarPorId(id);
        if(!cliente.isPresent()){
            result.addError(new ObjectError("cliente", "Cliente não encontrado"));
        }


        auditoria.post(this.converterCadastroClienteDto(cliente.get()), clienteDto, "cliente");

        this.atualizarDadosCliente(cliente.get(), clienteDto, result);

        if(result.hasErrors()){
            log.error("Erro validando clinete: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        this.clienteService.persistir(cliente.get());
        response.setData(this.converterCadastroClienteDto(cliente.get()));
        return ResponseEntity.ok(response);

    }


    /**
     * Método responsável por deletar um cliente  dado um id
     *
     * @param id
     * @return ResponseEntity<Response<String>>
     */
    @DeleteMapping("/id/{id}")
    public ResponseEntity<Response<String>> deletar(@PathVariable("id") Long id){
        log.info("Removendo um cliente pelo id; {}", id);
        Response<String> response =  new Response<>();
        Optional<Cliente> cliente = this.clienteService.buscarPorId(id);

        if(!cliente.isPresent()){
            log.error("erro ao remover o cliente por ID: {} ser invalido", id);
            response.getErros().add("Erro ao remover o cliente. Cliente não encontrado para o id "+id);
            return ResponseEntity.badRequest().body(response);
        }


        auditoria.post(this.converterCadastroClienteDto(cliente.get()), new ClienteDto(), "cliente");

        this.clienteService.remover(id);
        return  ResponseEntity.ok(response);


    }


    /**
     * Método responsável por Atualizar dados do cliente nos dados encontrados no DTO
     *
     * @param cliente
     * @param clienteDto
     * @param result
     */
    private void atualizarDadosCliente(Cliente cliente, ClienteDto clienteDto, BindingResult result) {

        cliente.setNome(clienteDto.getNome());
        cliente.setEmail(clienteDto.getEmail());
        cliente.setTelefone(clienteDto.getTelefone());
        cliente.setCpf(clienteDto.getCpf());

    }


    /**
     * Popular o DTO de cadastro com os dados do cliente
     *
     * @param cliente
     * @return ClienteDto
     */
    private ClienteDto converterCadastroClienteDto(Cliente cliente) {
        ClienteDto clienteDto = new ClienteDto();

        clienteDto.setId(cliente.getId());
        clienteDto.setNome(cliente.getNome());
        clienteDto.setCpf(cliente.getCpf());
        clienteDto.setEmail(cliente.getEmail());
        clienteDto.setTelefone(cliente.getTelefone());

        return clienteDto;
    }


    /**
     * Método responsável por verificar se o cliente exite no banco de dados
     *
     * @param clienteDto
     * @param result
     */
    private void validarDadosExistentes(ClienteDto clienteDto, BindingResult result) {

        this.clienteService.buscarPorCpf(clienteDto.getCpf())
                .ifPresent(emp -> result.addError(new ObjectError("cliente","CPF já existente.")));
        this.clienteService.buscarPorEmail(clienteDto.getEmail())
                .ifPresent(emp -> result.addError(new ObjectError("cliente","EMAIL já existente")));

    }


    /**
     * Método converte dados do Objeto DTO para cliente
     *
     * @param clienteDto
     * @param result
     * @return Cliente
     */
    private Cliente converterDtoParaCliente(ClienteDto clienteDto, BindingResult result){
        Cliente cliente = new Cliente();

        cliente.setId(clienteDto.getId());
        cliente.setNome(clienteDto.getNome());
        cliente.setCpf(clienteDto.getCpf());
        cliente.setEmail(clienteDto.getEmail());
        cliente.setTelefone(clienteDto.getTelefone());

        return cliente;
    }
}
