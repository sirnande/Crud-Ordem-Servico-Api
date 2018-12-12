package com.para.crudos.api.dtos;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

public class TecnicoDto {

    private Long id;
    private String nome;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotEmpty(message = "O nome não deve ser vazio")
    @Length(min = 3, max = 200, message = "O nome deve conter no minimo 3 e no maximo 200 caracteres")
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "TecnicoDto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}
