package br.com.macgarcia.gestorpessoal.validacao.impl;

import br.com.macgarcia.gestorpessoal.service.UsuarioService;
import br.com.macgarcia.gestorpessoal.validacao.ValidacaoUsuario;

public class ValidarUnicidadeUsuario extends ValidacaoUsuario {

	private UsuarioService service;
	
	public ValidarUnicidadeUsuario(UsuarioService service) {
		this.service = service;
	}

	@Override
	public Integer existenciaDeUsuarioComEmail(String email) {
		return service.buscarExistenciaDoUsuarioPeloEmail(email);
	}

	@Override
	public Integer existenciaDeUsuarioComLogin(String login) {
		return service.buscarExistenciaDoUsuarioPeloLogin(login);
	}
}
