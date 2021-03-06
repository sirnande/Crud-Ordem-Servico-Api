package com.para.crudos.api.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "endereco")
public class Endereco  implements Serializable{

	private Long id;
	private String cep;
	private String rua;
	private String bairro;
	private String cidade;
	private String estado;
	private List<OrdemServico> os;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_endereco")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}


	@Column(name = "cep", nullable = false)
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
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
	
	
	
	@Column(name = "estado", nullable = false)
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	
	
	@OneToMany(mappedBy = "endereco", fetch = FetchType.LAZY)
	public List<OrdemServico> getOs() {
		return os;
	}
	public void setOs(List<OrdemServico> os) {
		this.os = os;
	}


	@Override
	public String toString() {
		return "Endereco{" +
				"id=" + id +
				", cep='" + cep + '\'' +
				", rua='" + rua + '\'' +
				", bairro='" + bairro + '\'' +
				", cidade='" + cidade + '\'' +
				", estado='" + estado + '\'' +
				", os=" + os +
				'}';
	}
}
