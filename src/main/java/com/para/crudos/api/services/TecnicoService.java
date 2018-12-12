package com.para.crudos.api.services;

import com.para.crudos.api.model.Tecnico;

import java.util.List;
import java.util.Optional;

public interface TecnicoService {

    /**
     * Persistir um tecnico  na base dedados
     *
     * @param tecnico
     * @return Tecnico
     */
    Tecnico persistir(Tecnico tecnico);


    /**
     * Buscar e retorna um tecnipor por id
     *
     * @param id
     * @return Optional<Tecnico>
     */
    Optional<Tecnico> buscarPorId(Long id);


    /**
     * Buscar e retorna uma lista de tecnico dado um nome
     *
     * @param nome
     * @return List<Tecnico>
     */
    Optional<Tecnico> buscarPorNome(String nome);


    /**
     * Deleteandoum tecnico por Id
     *
     * @param id
     */
    void remover(Long id);


}
