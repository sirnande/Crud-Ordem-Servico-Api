package com.para.crudos.api.auditoria;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.para.crudos.api.dtos.AuditoriaDTO;
import com.para.crudos.api.exceptions.ValidacaoException;
import org.springframework.web.client.RestTemplate;

public class Auditoria<T> {

    public void post(T objetoAntigo, T objetoNovo, String entidade){
        try{
            final String url = "http://localhost:8090/crud/api/auditoria/save/";
            ObjectMapper mapper =  new ObjectMapper();

            AuditoriaDTO auditoriaDto = new AuditoriaDTO();
            auditoriaDto.setObjetoAntigo(mapper.writeValueAsString(objetoAntigo));
            auditoriaDto.setObjetoNovo(mapper.writeValueAsString(objetoNovo));
            auditoriaDto.setEntidade(entidade);

            RestTemplate restTemplate = new RestTemplate();

            AuditoriaDTO result = restTemplate.postForObject(url, auditoriaDto, AuditoriaDTO.class);

            System.out.println(result);

        }catch(Exception ex){
            throw new ValidacaoException("Erro ao se conectar com o servidor de auditoria");
        }

    }

}
