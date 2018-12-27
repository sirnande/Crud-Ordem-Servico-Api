package com.para.crudos.api.dtos;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public class OrdemServicoDTO implements Serializable {

    private Long id;

    private String especificacao;

    private String dataAberta;

    private String dataFinalizada;

    private String status;

    private Long clienteId;

    private Long tecnicoId;

    private Long enderecoId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    @NotEmpty(message = "A especificaçao não deve ser vazio")
    public String getEspecificacao() {
        return especificacao;
    }

    public void setEspecificacao(String especificacao) {
        this.especificacao = especificacao;
    }

    @NotEmpty(message = "Data de abertura não deve ser vazio")
    public String getDataAberta() {
        return dataAberta;
    }

    public void setDataAberta(String dataAberta) {
        this.dataAberta = dataAberta;
    }

    public String getDataFinalizada() {
        return dataFinalizada;
    }

    public void setDataFinalizada(String dataFinalizada) {
        this.dataFinalizada = dataFinalizada;
    }


    @NotEmpty(message = "O Status da ordem de servico não deve ser vazio")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public Long getCliente() {
        return clienteId;
    }

    public void setCliente(Long cliente) {
        this.clienteId = cliente;
    }


    public Long getTecnico() {
        return tecnicoId;
    }

    public void setTecnico(Long tecnico) {
        this.tecnicoId = tecnico;
    }


    public Long getEndereco() {
        return enderecoId;
    }

    public void setEndereco(Long endereco) {
        this.enderecoId = endereco;
    }

    @Override
    public String toString() {
        return "OrdemServicoDTO{" +
                "id=" + id +
                ", especificacao='" + especificacao + '\'' +
                ", dataAberta='" + dataAberta + '\'' +
                ", dataFinalizada='" + dataFinalizada + '\'' +
                ", status='" + status + '\'' +
                ", cliente=" + clienteId +
                ", tecnico=" + tecnicoId +
                ", endereco='" + enderecoId + '\'' +
                '}';
    }
}
