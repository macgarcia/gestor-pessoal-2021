package br.com.macgarcia.gestorpessoal.DTO.saida;

import java.io.Serializable;

import br.com.macgarcia.gestorpessoal.model.Usuario;

public class UsuarioDtoSaida implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String nome;
	private String login;
	private String email;
	
	@Deprecated
	public UsuarioDtoSaida() {}
	
	public UsuarioDtoSaida(Usuario usuario) {
		this.id = usuario.getId();
		this.nome = usuario.getNome();
		this.login = usuario.getLogin();
		this.email = usuario.getEmail();
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;	
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
