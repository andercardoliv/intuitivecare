package br.com.intuitivecare.desafio.model;

import static javax.persistence.GenerationType.AUTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CategoriaPadrao {
	
	@Id
	@GeneratedValue(strategy = AUTO)
	private long id;
	
	@Column(unique = true)
	private int codigo;
	
	@Column
	private String descricao;

	protected CategoriaPadrao() {}
	
	public CategoriaPadrao(int codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
