package br.com.macgarcia.gestorpessoal.DTO.saida;

import java.io.Serializable;

import br.com.macgarcia.gestorpessoal.model.Anotacao;

public class AnotacaoDtoSaida implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String titulo;
	private String descricao;
	
	@Deprecated
	public AnotacaoDtoSaida() {}

	public AnotacaoDtoSaida(Anotacao e) {
		this.id = e.getId();
		this.titulo = e.getTitulo();
		this.descricao = e.getDescricao();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}
