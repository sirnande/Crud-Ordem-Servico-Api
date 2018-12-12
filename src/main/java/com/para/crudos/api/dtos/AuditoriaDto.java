package com.para.crudos.api.dtos;


public class AuditoriaDto {


    private String id;
    private String objetoAntigo;
    private String objetoNovo;
    private String entidade;
    private String data_modificacao;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getObjetoAntigo() {
        return objetoAntigo;
    }

    public void setObjetoAntigo(String objetoAntigo) {
        this.objetoAntigo = objetoAntigo;
    }



    public String getObjetoNovo() {
        return objetoNovo;
    }

    public void setObjetoNovo(String objetoNovo) {
        this.objetoNovo = objetoNovo;
    }




    public String getEntidade() {
        return entidade;
    }

    public void setEntidade(String entidade) {
        this.entidade = entidade;
    }





    public String getData_modificacao() {
        return data_modificacao;
    }

    public void setData_modificacao(String data_modificacao) {
        this.data_modificacao = data_modificacao;
    }

    @Override
    public String toString() {
        return "AuditoriaDto{" +
                "id='" + id + '\'' +
                ", objetoAntigo='" + objetoAntigo + '\'' +
                ", objetoNovo='" + objetoNovo + '\'' +
                ", entidade='" + entidade + '\'' +
                ", data_modificacao='" + data_modificacao + '\'' +
                '}';
    }
}
