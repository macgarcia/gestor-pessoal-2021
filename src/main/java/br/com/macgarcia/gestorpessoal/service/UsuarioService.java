package br.com.macgarcia.gestorpessoal.service;

import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.macgarcia.gestorpessoal.DTO.entrada.UsuarioDtoEntrada;
import br.com.macgarcia.gestorpessoal.DTO.saida.UsuarioDtoSaida;
import br.com.macgarcia.gestorpessoal.model.Usuario;
import br.com.macgarcia.gestorpessoal.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository dao;

	public boolean verificar(Long id) {
		Optional<Usuario> possivelUsuario = dao.findById(id);
		if (possivelUsuario.isPresent()) {
			return true;
		}
		return false;
	}

	public UsuarioDtoSaida getUsuarioPorId(Long id) {
		return new UsuarioDtoSaida(dao.findById(id).get());	
	}

	public Integer buscarExistenciaDoUsuarioPeloEmail(String email) {
		return dao.findByEmail(email);
	}
	
	public Integer buscarExistenciaDoUsuarioPeloLogin(String login) {
		return dao.findByLogin(login);
	}

	@Transactional
	public void salvar(@Valid UsuarioDtoEntrada dto) {
		Usuario novoUsuario = dto.criar();
		dao.saveAndFlush(novoUsuario);
	}
	
	private boolean validaUpdate(Long id, UsuarioDtoEntrada dto) {
		Optional<Long> idUsuario = dao.buscarIdPeloLogin(dto.getLogin());
		if (idUsuario.isPresent() && !idUsuario.get().equals(id)) {
			return false;
		}
		idUsuario = dao.buscarIdPeloEmail(dto.getEmail());
		if (idUsuario.isPresent() && !idUsuario.get().equals(id)) {
			return false;
		}
		return true;
	}
	
	@Transactional
	public boolean atualizar(Long id, UsuarioDtoEntrada dto) {
		if (validaUpdate(id, dto)) {
			Usuario usuarioExistente = dao.findById(id).get();
			Usuario usuarioAtualizado = dto.atualizar(usuarioExistente);
			dao.saveAndFlush(usuarioAtualizado);
			return true;
		}
		return false;
	}

	public Optional<UsuarioDtoSaida> logar(String login, String senha) {
		Optional<Usuario> possivelUsuario = dao.findByLoginAndSenha(login, senha);
		if (possivelUsuario.isPresent()) {
			return Optional.of(new UsuarioDtoSaida(possivelUsuario.get()));
		}
		return Optional.empty();
	}

}
