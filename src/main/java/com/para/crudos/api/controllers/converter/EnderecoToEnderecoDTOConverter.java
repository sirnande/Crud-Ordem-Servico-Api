package com.para.crudos.api.controllers.converter;

import com.para.crudos.api.dtos.EnderecoDTO;
import com.para.crudos.api.model.Endereco;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EnderecoToEnderecoDTOConverter implements Converter<Endereco, EnderecoDTO> {
    @Override
    public EnderecoDTO convert(Endereco endereco) {
        EnderecoDTO enderecoDto = new EnderecoDTO();

        enderecoDto.setId(endereco.getId());
        enderecoDto.setCep(endereco.getCep());
        enderecoDto.setRua(endereco.getRua());
        enderecoDto.setBairro(endereco.getBairro());
        enderecoDto.setCidade(endereco.getCidade());
        enderecoDto.setEstado(endereco.getEstado());

        return enderecoDto;
    }
}
