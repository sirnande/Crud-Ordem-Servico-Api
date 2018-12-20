package com.para.crudos.api.controllers.converter;

import com.para.crudos.api.dtos.ClienteDTO;
import com.para.crudos.api.model.Cliente;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ClienteToClienteDTOConverter implements Converter<Cliente, ClienteDTO> {

    @Override
    public ClienteDTO convert(Cliente cliente) {
        ClienteDTO clienteDto = new ClienteDTO();

        clienteDto.setId(cliente.getId());
        clienteDto.setNome(cliente.getNome());
        clienteDto.setCpf(cliente.getCpf());
        clienteDto.setEmail(cliente.getEmail());
        clienteDto.setTelefone(cliente.getTelefone());

        return clienteDto;
    }
}
