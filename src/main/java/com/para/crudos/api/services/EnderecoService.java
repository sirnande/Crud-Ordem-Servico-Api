package com.para.crudos.api.services;

import com.para.crudos.api.model.Endereco;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface EnderecoService {


    /**
     * Pessitir um endereco na base de dados
     *
     * @param endereco
     * @return Endereco
     */
    Endereco persistir(Endereco endereco);


    /**
     * Buscar e retorna um endereço dado um cep
     *
     * @param cep
     * @return Endereco
     */
    Optional<Endereco> buscarPorCep(String cep);


    /**
     * Buscar e retorna uma lista de endereco dado uma rua
     *
     * @param rua
     * @return List<Endereco>
     */
     Optional<List<Endereco>> buscarPorRua(String rua);


    /**
     * Buscar e retorna uma lista de endereco dado uam cidade
     *
     * @param cidade
     * @return List<Endereco>
     */
    Optional<List<Endereco>> buscarPorCidade(String cidade);


    /**
     * Buscar endereco por id
     *
     * @param id
     * @return Endereco
     */
    Optional<Endereco> buscarPorId(Long id);


    /**
     * Deletando um endereco por id
     *
     * @param id
     */
    void remover(Long id);


}
