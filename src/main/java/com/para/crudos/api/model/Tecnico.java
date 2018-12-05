package com.para.crudos.api.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tecnico")
public class Tecnico implements Serializable{
	
	private Long id;
	private String nome;
	private List<OrdemServico> os;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "nome", nullable = false)
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	@OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<OrdemServico> getOs() {
		return os;
	}
	public void setOs(List<OrdemServico> os) {
		this.os = os;
	}
	@Override
	public String toString() {
		return "Tecnico [id=" + id + ", nome=" + nome + "]";
	}
	

}
