package com.para.crudos.api.controllers.converter;

import com.para.crudos.api.dtos.EnderecoDTO;
import com.para.crudos.api.model.Endereco;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EnderecoDTOToEnderecoConverter implements Converter<EnderecoDTO, Endereco> {
    @Override
    public Endereco convert(EnderecoDTO enderecoDTO) {
        Endereco endereco = new Endereco();

        endereco.setId(enderecoDTO.getId());
        endereco.setCep(enderecoDTO.getCep());
        endereco.setRua(enderecoDTO.getRua());
        endereco.setBairro(enderecoDTO.getBairro());
        endereco.setCidade(enderecoDTO.getCidade());
        endereco.setEstado(enderecoDTO.getEstado());

        return endereco;
    }
}
