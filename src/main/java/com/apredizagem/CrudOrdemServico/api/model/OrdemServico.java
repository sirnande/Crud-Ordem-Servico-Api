package com.apredizagem.CrudOrdemServico.api.model;

import com.apredizagem.CrudOrdemServico.api.enums.Situacao;

import javax.persistence.*;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Date;

@Entity
@Table(name = "ordemservico")
public class OrdemServico  implements Serializable {

    private Long id;
    private Situacao situacao;
    private String especificacao;
    private String laudoTecnico;
    private Date inicio;
    private Date fim;
    private Cliente cliente;
    private Tecnico tecnico;
    private Endereco endereco;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false)
    public Situacao getSituacao() {
        return situacao;
    }

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }


    @Column(name = "especificacao", nullable = false)
    public String getEspecificacao() {
        return especificacao;
    }

    public void setEspecificacao(String especificacao) {
        this.especificacao = especificacao;
    }

    @Column(name = "laudotecnico", nullable = true)
    public String getLaudoTecnico() {
        return laudoTecnico;
    }

    public void setLaudoTecnico(String laudoTecnico) {
        this.laudoTecnico = laudoTecnico;
    }

    @Column(name = "    inicio", nullable = false)
    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }


    @Column(name = "fim", nullable = true)
    public Date getFim() {
        return fim;
    }

    public void setFim(Date fim) {
        this.fim = fim;
    }


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cpf")
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    public Tecnico getTecnico() {
        return tecnico;
    }

    public void setTecnico(Tecnico tecnico) {
        this.tecnico = tecnico;
    }


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cep")
    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }


    @Override
    public String toString() {
        return "OrdemServico{" +
                "id=" + id +
                ", situacao=" + situacao +
                ", especificacao='" + especificacao + '\'' +
                ", laudoTecnico='" + laudoTecnico + '\'' +
                ", inicio=" + inicio +
                ", fim=" + fim +
                ", cliente=" + cliente +
                ", tecnico=" + tecnico +
                ", endereco=" + endereco +
                '}';
    }
}
