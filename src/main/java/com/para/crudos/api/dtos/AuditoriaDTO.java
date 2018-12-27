package com.para.crudos.api.dtos;


public class AuditoriaDTO {


    private String id;
    private String objetoAntigo;
    private String objetoNovo;
    private String entidade;
    private String dataModificacao;

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
        return dataModificacao;
    }

    public void setData_modificacao(String dataModificacao) {
        this.dataModificacao = dataModificacao;
    }

    @Override
    public String toString() {
        return "AuditoriaDTO{" +
                "id='" + id + '\'' +
                ", objetoAntigo='" + objetoAntigo + '\'' +
                ", objetoNovo='" + objetoNovo + '\'' +
                ", entidade='" + entidade + '\'' +
                ", data_modificacao='" + dataModificacao + '\'' +
                '}';
    }
}
