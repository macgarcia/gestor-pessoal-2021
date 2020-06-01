package br.com.macgarcia.gestorpessoal.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.macgarcia.gestorpessoal.DTO.entrada.AnotacaoDtoEntrada;
import br.com.macgarcia.gestorpessoal.DTO.saida.AnotacaoDtoSaida;
import br.com.macgarcia.gestorpessoal.model.Anotacao;
import br.com.macgarcia.gestorpessoal.model.Usuario;
import br.com.macgarcia.gestorpessoal.repository.AnotacaoRepository;
import br.com.macgarcia.gestorpessoal.repository.UsuarioRepository;

@Service
public class AnotacaoService {

	@Autowired
	private AnotacaoRepository dao;
	@Autowired
	private UsuarioRepository usuarioDao;

	public List<AnotacaoDtoSaida> buscarAnotacoes() {
		Stream<Anotacao> anotacoes = dao.findAll().stream();
		return anotacoes
				.sorted(Comparator.comparing(Anotacao::getId).reversed())
				.map(e -> {return new AnotacaoDtoSaida(e);})
				.collect(Collectors.toList());
	}

	@Transactional
	public void salvarAnotacao(AnotacaoDtoEntrada dto) {
		Usuario usuario = usuarioDao.findById(dto.getIdUsuario()).get();
		Anotacao novaAnotacao = dto.criar(usuario);
		dao.saveAndFlush(novaAnotacao);
	}

	@Transactional
	public void atualizarAnotacao(Long id, AnotacaoDtoEntrada dto) {
		Anotacao anotacaoExistente = dao.findById(id).get();
		Anotacao anotacaoAtualizada = dto.atualizar(anotacaoExistente);
		dao.saveAndFlush(anotacaoAtualizada);
	}

	@Transactional
	public void apagarAnotacao(Long idAnotacao) {
		dao.deleteById(idAnotacao);
	}

	public Optional<AnotacaoDtoSaida> buscarUnica(Long id) {
		Optional<Anotacao> possivelAnotacao = dao.findById(id);
		if (possivelAnotacao.isPresent()) {
			return Optional.of(new AnotacaoDtoSaida(possivelAnotacao.get()));
		}
		return Optional.empty();
	}

}
