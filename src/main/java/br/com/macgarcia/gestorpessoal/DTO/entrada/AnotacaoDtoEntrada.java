package br.com.macgarcia.gestorpessoal.DTO.entrada;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.macgarcia.gestorpessoal.model.Anotacao;
import br.com.macgarcia.gestorpessoal.model.Usuario;

public class AnotacaoDtoEntrada implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "Titulo da anotação é obrigatório")
	private String titulo;
	
	@NotBlank(message = "Descrição da anotação é obrigatória")
	private String descricao;
	
	@NotNull(message = "Identificador do usuário é obrigátorio")
	private Long idUsuario;

	@Deprecated
	public AnotacaoDtoEntrada() {
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

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Anotacao criar(Usuario usuario) {
		return new Anotacao(titulo, descricao, usuario);
	}

	public Anotacao atualizar(Anotacao anotacaoExistente) {
		anotacaoExistente.setTitulo(titulo);
		anotacaoExistente.setDescricao(descricao);
		return anotacaoExistente;
	}

}
