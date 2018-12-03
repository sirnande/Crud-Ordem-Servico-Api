package com.apredizagem.CrudOrdemServico.api.model;

import com.apredizagem.CrudOrdemServico.api.enums.UF;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "endereco")
public class Endereco  implements Serializable {

    private Long cep;
    private String rua;
    private String bairro;
    private String cidade;
    private UF uf;


    @Id
    @Column(name = "cep", nullable = false)
    public Long getCep() {
        return cep;
    }

    public void setCep(Long cep) {
        this.cep = cep;
    }


    @Column(name = "rua", nullable = false)
    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }


    @Column(name = "bairro", nullable = false)
    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }


    @Column(name = "cidade", nullable = false)
    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }


    @Enumerated(EnumType.STRING)
    @Column(name = "uf", nullable = false)
    public UF getUf() {
        return uf;
    }

    public void setUf(UF uf) {
        this.uf = uf;
    }

    @Override
    public String toString() {
        return "Endereco{" +
                "cep='" + cep + '\'' +
                ", rua='" + rua + '\'' +
                ", bairro='" + bairro + '\'' +
                ", cidade='" + cidade + '\'' +
                ", uf=" + uf +
                '}';
    }
}
