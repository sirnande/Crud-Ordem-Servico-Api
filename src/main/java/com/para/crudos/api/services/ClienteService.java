package com.para.crudos.api.services;

import com.para.crudos.api.model.Cliente;

import java.util.Optional;

public interface ClienteService {

    /**
     *
     * Persistir um cliente na base de dado
     *
     * @param cliente
     * @return Cliente
     */

    Cliente persistir(Cliente cliente);


    /**
     * Buscar um cliente dado um id
     *
     * @param id
     * @return Optional<Cliente>
     */
    Optional<Cliente> buscarPorId(Long id);



    /**
     *
     * Buscar e retorna um Cliente dado um cpf
     *
     * @param cpf
     * @return Cliente
     */
    Optional<Cliente> buscarPorCpf(String cpf);


    /**
     *
     * Buscar e retorna um Cliente dado o nome
     *
     * @param nome
     * @return Cliente
     */
    Optional<Cliente> buscarPorNome(String nome);


    /**
     * Buscar e retorna um Cliente dado um telefone
     *
     * @param telefone
     * @return Cliente
     */
    Optional<Cliente> buscarPorTelefone(String telefone);


    /**
     * Buscar e retorna um Cliente por email
     *
     * @param email
     * @return Cliente
     */
    Optional<Cliente> buscarPorEmail(String email);


    /**Reover um cliente da base de dados
     *
     * @param id
     */
    void remover(Long id);

}
