package com.para.crudos.api.controllers.converter;

import com.para.crudos.api.dtos.OrdemServicoDTO;
import com.para.crudos.api.model.OrdemServico;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class OrdemServicoToOrdemServicoDTOConverter implements Converter<OrdemServico, OrdemServicoDTO> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public OrdemServicoDTO convert(OrdemServico ordemServico) {
        OrdemServicoDTO ordemServicoDTO = new OrdemServicoDTO();

        ordemServicoDTO.setId(ordemServico.getId());
        ordemServicoDTO.setEspecificacao(ordemServico.getEspecificacao());
        ordemServicoDTO.setStatus(ordemServico.getStatus().name());
        ordemServicoDTO.setDataAberta(dateFormat.format(ordemServico.getDataAberta()));
        if(ordemServico.getDataFinalizada() != null)
            ordemServicoDTO.setDataFinalizada(dateFormat.format(ordemServico.getDataFinalizada()));
        ordemServicoDTO.setEndereco(ordemServico.getEndereco().getId());
        ordemServicoDTO.setCliente(ordemServico.getCliente().getId());
        ordemServicoDTO.setTecnico(ordemServico.getTecnico().getId());

        return ordemServicoDTO;
    }
}
