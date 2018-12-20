package com.para.crudos.api.controllers.converter;

import com.para.crudos.api.dtos.ClienteDTO;
import com.para.crudos.api.model.Cliente;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ClienteDTOToClienteConverter implements Converter<ClienteDTO, Cliente> {

    @Override
    public Cliente convert(ClienteDTO clienteDto) {
        Cliente cliente = new Cliente();

        cliente.setId(clienteDto.getId());
        cliente.setNome(clienteDto.getNome());
        cliente.setCpf(clienteDto.getCpf());
        cliente.setEmail(clienteDto.getEmail());
        cliente.setTelefone(clienteDto.getTelefone());

        return cliente;
    }
}
