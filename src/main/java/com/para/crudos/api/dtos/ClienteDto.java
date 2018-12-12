package com.para.crudos.api.dtos;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public class ClienteDto implements Serializable {

    private Long id;
    private String nome;
    private String telefone;
    private String email;
    private String cpf;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @NotEmpty(message = "Nome não deve ser vazio")
    @Length(min = 5, max = 200, message = "Nome deve conter entre 5 e 200 caracteres")
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    @NotEmpty(message = "O telefone não deve ser vazio")
    @Length(min = 3, max = 11, message = "O telefone de conter no minimo  e nomaximo 11 caractere")
    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @NotEmpty(message = "O email nao deve ser vazio")
    @Length(min = 5, max = 30, message = "O email deve conter no minimo 5 e no maximo 30 caracteres")
    @Email(message = "E-mail invalido")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotEmpty(message = "O cpf nao deve ser vazio")
    @CPF(message = "CPF invalido")
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public String toString() {
        return "ClienteDto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", telefone='" + telefone + '\'' +
                ", email='" + email + '\'' +
                ", cpf='" + cpf + '\'' +
                '}';
    }
}
