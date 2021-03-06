package com.para.crudos.api.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@Entity
//@Table(name = "cliente")
public class Cliente implements Serializable{
	
	private Long id;
	private String nome;
	private String telefone;
	private String email;
	private String cpf;
	private List<OrdemServico> os;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cliente")
	@Column(name = "id")
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
	
	
	@Column(name = "telefone", nullable = false)
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	@Column(name = "email", nullable = false)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	@Column(name = "cpf", nullable = false)
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	
	@OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
	public List<OrdemServico> getOs() {
		return os;
	}
	public void setOs(List<OrdemServico> os) {
		this.os = os;
	}


	@Override
	public String toString() {
		return "Cliente{" +
				"id=" + id +
				", nome='" + nome + '\'' +
				", telefone='" + telefone + '\'' +
				", email='" + email + '\'' +
				", cpf='" + cpf + '\'' +
				'}';
	}
}
