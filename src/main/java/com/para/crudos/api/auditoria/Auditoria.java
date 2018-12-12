package com.para.crudos.api.auditoria;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.para.crudos.api.dtos.AuditoriaDto;
import org.springframework.web.client.RestTemplate;

public class Auditoria<T> {

    public void post(T objetoAntigo, T objetoNovo, String entidade){
        try{
            final String url = "http://localhost:8090/crud/api/auditoria/save/";
            ObjectMapper mapper =  new ObjectMapper();

            AuditoriaDto auditoriaDto = new AuditoriaDto();
            auditoriaDto.setObjetoAntigo(mapper.writeValueAsString(objetoAntigo));
            auditoriaDto.setObjetoNovo(mapper.writeValueAsString(objetoNovo));
            auditoriaDto.setEntidade(entidade);

            RestTemplate restTemplate = new RestTemplate();

            AuditoriaDto result = restTemplate.postForObject(url, auditoriaDto, AuditoriaDto.class);

            System.out.println(result);

        }catch(Exception ex){
            throw new RuntimeException(ex);
        }

    }

}
