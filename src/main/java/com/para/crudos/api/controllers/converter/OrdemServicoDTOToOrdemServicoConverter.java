package com.para.crudos.api.controllers.converter;

import com.para.crudos.api.dtos.OrdemServicoDTO;
import com.para.crudos.api.enums.Status;
import com.para.crudos.api.exceptions.ValidacaoException;
import com.para.crudos.api.model.Cliente;
import com.para.crudos.api.model.Endereco;
import com.para.crudos.api.model.OrdemServico;
import com.para.crudos.api.model.Tecnico;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class OrdemServicoDTOToOrdemServicoConverter implements Converter<OrdemServicoDTO, OrdemServico> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public OrdemServico convert(OrdemServicoDTO ordemServicoDTO) {
        OrdemServico ordemServico = new OrdemServico();

        ordemServico.setId(ordemServicoDTO.getId());
        ordemServico.setEspecificacao(ordemServicoDTO.getEspecificacao());

        if(!EnumUtils.isValidEnum(Status.class, ordemServicoDTO.getStatus())){
            throw new ValidacaoException("Status inválido");
        }

        ordemServico.setStatus(Status.valueOf(ordemServicoDTO.getStatus()));

        try {
            ordemServico.setDataAberta(dateFormat.parse(ordemServicoDTO.getDataAberta()));
            if (ordemServicoDTO.getDataFinalizada() != null)
                ordemServico.setDataFinalizada(dateFormat.parse(ordemServicoDTO.getDataFinalizada()));
        }catch (ParseException e){
            throw new ValidacaoException("Data invalida");
        }
        Endereco endereco = new Endereco();
        endereco.setId(ordemServicoDTO.getEndereco());
        ordemServico.setEndereco(endereco);

        Cliente cliente = new Cliente();
        cliente.setId(ordemServicoDTO.getCliente());
        ordemServico.setCliente(cliente);

        Tecnico tecnico = new Tecnico();
        tecnico.setId(ordemServicoDTO.getTecnico());
        ordemServico.setTecnico(tecnico);

        return ordemServico;
    }
}
