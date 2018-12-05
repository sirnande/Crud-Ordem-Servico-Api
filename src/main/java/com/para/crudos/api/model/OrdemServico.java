package com.para.crudos.api.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.para.crudos.api.enums.Status;

import net.bytebuddy.matcher.FailSafeMatcher;

@Entity
@Table(name = "ordemservico")
public class OrdemServico implements Serializable{
	
	private Long id;
	private String especificacao;
	private Date dataAberta;
	private Date dataFinalizada;
	private Status status;
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
	
	
	@Column(name = "especificacao", nullable = false)
	public String getEspecificacao() {
		return especificacao;
	}
	public void setEspecificacao(String especificacao) {
		this.especificacao = especificacao;
	}
	
	@Column(name = "data_aberta", nullable = false)
	public Date getDataAberta() {
		return dataAberta;
	}
	public void setDataAberta(Date dataAberta) {
		this.dataAberta = dataAberta;
	}
	
	
	@Column(name = "data_finalizada", nullable = true)
	public Date getDataFinalizada() {
		return dataFinalizada;
	}
	public void setDataFinalizada(Date dataFinalizada) {
		this.dataFinalizada = dataFinalizada;
	}

	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cliente_id", nullable = false) 
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "tecnico_id", nullable = false)
	public Tecnico getTecnico() {
		return tecnico;
	}
	public void setTecnico(Tecnico tecnico) {
		this.tecnico = tecnico;
	}
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "endereco_id", nullable = false)
	public Endereco getEndereco() {
		return endereco;
	}
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	
	
	
	@Override
	public String toString() {
		return "OrdemServico [id=" + id + ", especificacao=" + especificacao + ", dataAberta=" + dataAberta
				+ ", dataFinalizada=" + dataFinalizada + ", status=" + status + ", cliente=" + cliente + ", tecnico="
				+ tecnico + ", endereco=" + endereco + "]";
	}


}
