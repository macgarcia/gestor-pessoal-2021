package br.com.macgarcia.gestorpessoal.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "USUARIO")
public class Usuario extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "Login do usuário é obrigatório")
	@Column(name = "LOGIN", unique = true)
	private String login;
	
	@NotBlank(message = "Senha do usuário é obrigatória")
	@Column(name = "SENHA")
	private String senha;
	
	@NotBlank(message = "Nome do usuário é obrigatório")
	@Column(name = "NOME")
	private String nome;
	
	@Email
	@NotBlank(message = "E-mail do usuário é obrigatório")
	@Column(name = "EMAIL", unique = true)
	private String email;
	
	@Deprecated
	public Usuario() {}

	public Usuario(@NotBlank(message = "Nome não pode estar em branco") String nome,
			@NotBlank(message = "Login não pode estar em branco") String login,
			@NotBlank(message = "Senha não pode esta em branco") String senha,
			@Email @NotBlank(message = "E-mail não pode estar em branco") String email) {
		this.nome = nome;
		this.login = login;
		this.senha = senha;
		this.email = email;
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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}	
}
