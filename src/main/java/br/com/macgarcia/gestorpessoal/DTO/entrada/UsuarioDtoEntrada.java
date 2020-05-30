package br.com.macgarcia.gestorpessoal.DTO.entrada;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import br.com.macgarcia.gestorpessoal.model.Usuario;

public class UsuarioDtoEntrada implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotBlank(message = "Nome não pode estar em branco")
	private String nome;
	
	@NotBlank(message = "Login não pode estar em branco")
	private String login;
	
	@NotBlank(message = "Senha não pode esta em branco")
	private String senha;
	
	@Email
	@NotBlank(message = "E-mail não pode estar em branco")
	private String email;
	
	@Deprecated
	public UsuarioDtoEntrada() {}

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

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Usuario criar() {
		return new Usuario(nome, login, senha, email);
	}

	public Usuario atualizar(Usuario usuarioExistente) {
		usuarioExistente.setNome(this.nome);
		usuarioExistente.setSenha(this.senha);
		return usuarioExistente;
	}	
}
