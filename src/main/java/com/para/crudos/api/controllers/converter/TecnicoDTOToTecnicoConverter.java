package com.para.crudos.api.controllers.converter;

import com.para.crudos.api.dtos.TecnicoDTO;
import com.para.crudos.api.model.Tecnico;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TecnicoDTOToTecnicoConverter implements Converter<TecnicoDTO, Tecnico> {
    @Override
    public Tecnico convert(TecnicoDTO tecnicoDto) {
        Tecnico tecnico = new Tecnico();

        tecnico.setId(tecnicoDto.getId());
        tecnico.setNome(tecnicoDto.getNome());

        return tecnico;
    }
}
