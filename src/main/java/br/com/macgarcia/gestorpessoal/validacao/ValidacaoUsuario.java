package br.com.macgarcia.gestorpessoal.validacao;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.macgarcia.gestorpessoal.DTO.entrada.UsuarioDtoEntrada;

public abstract class ValidacaoUsuario implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return UsuarioDtoEntrada.class.isAssignableFrom(clazz);	
	}

	@Override
	public void validate(Object target, Errors errors) {
		UsuarioDtoEntrada dto = (UsuarioDtoEntrada) target;
		Integer count = existenciaDeUsuarioComEmail(dto.getEmail());
		Integer count2 = existenciaDeUsuarioComLogin(dto.getLogin());
		if (count > 0) {
			errors.reject("Email", null, "Email já cadastrado");
		}
		if (count2 > 0) {
			errors.reject("Login", null, "Login já cadastrado");
		}
	}
	public abstract Integer existenciaDeUsuarioComEmail(String email);
	public abstract Integer existenciaDeUsuarioComLogin(String login);
}
