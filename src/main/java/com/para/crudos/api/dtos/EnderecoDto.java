package com.para.crudos.api.dtos;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

public class EnderecoDto {

    private Long id;
    private String cep;
    private String rua;
    private String bairro;
    private String cidade;
    private String estado;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotEmpty(message = "O cep não deve ser vazio")
    @Length(min = 1, max = 10, message = "O cep deve ter no minimo 1  e no maximo 7 caracteres")
    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    @NotEmpty(message = "A rua não deve ser vazio")
    @Length(min = 5, max = 200, message = "A rua deve conter no minimo 5 e no maximo 200 caracteres")
    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }


    @NotEmpty(message = "O bairro nao deve ser vazio")
    @Length(min = 5, max = 200, message = "O mairro deve conter no minimo 5 e no maximo 200 caracteres")
    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }


    @NotEmpty(message = "A cidade nao deve ser vazio")
    @Length(min = 5, max = 200, message = "A cidade deve conter no minimo 5 e no maximo 200 caracteres")
    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    @NotEmpty(message = "O estado nao deve ser vazio")
    @Length(min = 2, max = 200, message = "O estado deve conter no minimo 2 e no maximo 200 caracteres")
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    @Override
    public String toString() {
        return "EnderecoDto{" +
                "id=" + id +
                ", cep='" + cep + '\'' +
                ", rua='" + rua + '\'' +
                ", bairro='" + bairro + '\'' +
                ", cidade='" + cidade + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
