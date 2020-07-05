package br.com.macgarcia.gestorpessoal.service;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

	public Page<AnotacaoDtoSaida> buscarTodasAnotacoes(Long idUsuario, Pageable page) {
		var anotacoes = dao.buscarAnotacoesPaginado(idUsuario, page);
		var list = anotacoes.stream().sorted(Comparator.comparing(Anotacao::getId))
				.map(e -> {return new AnotacaoDtoSaida(e);}).collect(Collectors.toList());
		return new PageImpl<AnotacaoDtoSaida>(list, anotacoes.getPageable(), anotacoes.getTotalElements());
	}
	
	public Page<AnotacaoDtoSaida> buscarAnotacoesPorPesquisa(Long idUsuario, String key, Pageable page) {
		var anotacoes = dao.buscarAnotacoesPaginado(idUsuario, page);
		var list = anotacoes.stream()
				.sorted(Comparator.comparing(Anotacao::getId))
				.filter(e -> e.getTitulo().toLowerCase().contains(key.toLowerCase()))
				.map(e -> {return new AnotacaoDtoSaida(e);})
				.collect(Collectors.toList());
		return new PageImpl<AnotacaoDtoSaida>(list, anotacoes.getPageable(), list.size());
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
