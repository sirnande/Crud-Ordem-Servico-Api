package com.para.crudos.api.services.implementations;

import com.para.crudos.api.model.Cliente;
import com.para.crudos.api.repositories.ClienteRepository;
import com.para.crudos.api.services.ClienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteServiceImplementation implements ClienteService {

    private static final Logger log = LoggerFactory.getLogger(ClienteServiceImplementation.class);

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public Cliente persistir(Cliente cliente) {
        log.info("Persistindo cliente: {}", cliente);
        return this.clienteRepository.save(cliente);
    }

    @Override
    public Optional<Cliente> buscarPorCpf(String cpf) {
        log.info("Buscando por cliente pelo cpf: }", cpf );
        return Optional.ofNullable(this.clienteRepository.findByCpf(cpf));
    }

    @Override
    public Optional<Cliente> buscarPorNome(String nome) {
        log.info("Busca cliente pelo nome: {}", nome);
        return Optional.ofNullable(this.clienteRepository.findByNome(nome));
    }

    @Override
    public Optional<Cliente> buscarPorTelefone(String telefone) {
        log.info("Buscar cliente pelo telefone: {}", telefone);
        return Optional.ofNullable(this.clienteRepository.findByTelefone(telefone));
    }

    @Override
    public Optional<Cliente> buscarPorEmail(String email) {
        log.info("bucar cliente pelo email: {}", email);
        return Optional.ofNullable(this.clienteRepository.findByEmail(email));
    }

}
