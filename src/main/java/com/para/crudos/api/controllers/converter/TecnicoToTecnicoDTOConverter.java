package com.para.crudos.api.controllers.converter;

import com.para.crudos.api.dtos.TecnicoDTO;
import com.para.crudos.api.model.Tecnico;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TecnicoToTecnicoDTOConverter implements Converter<Tecnico, TecnicoDTO> {

    @Override
    public TecnicoDTO convert(Tecnico tecnico) {
        TecnicoDTO tecnicoDTO = new TecnicoDTO();

        tecnicoDTO.setId(tecnico.getId());
        tecnicoDTO.setNome(tecnico.getNome());

        return tecnicoDTO;
    }
}
