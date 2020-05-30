package br.com.macgarcia.gestorpessoal.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ANOTACAO")
public class Anotacao extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "Titulo da anotação é obrigatório")
	@Column(name = "TITULO")
	private String titulo;
	
	@NotBlank(message = "Descrição da anotação é obrigatório")
	@Column(name = "DESCRICAO")
	private String descricao;
	
	@JsonIgnore	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario usuario;
	
	@Deprecated
	public Anotacao() {}

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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
