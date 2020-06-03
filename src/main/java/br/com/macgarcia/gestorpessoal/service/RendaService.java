package br.com.macgarcia.gestorpessoal.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.macgarcia.gestorpessoal.DTO.entrada.RendaDtoEntrada;
import br.com.macgarcia.gestorpessoal.DTO.saida.RendaDtoSaida;
import br.com.macgarcia.gestorpessoal.model.Renda;
import br.com.macgarcia.gestorpessoal.model.Usuario;
import br.com.macgarcia.gestorpessoal.repository.RendaRepository;
import br.com.macgarcia.gestorpessoal.repository.UsuarioRepository;

@Service
public class RendaService {

	@Autowired
	private RendaRepository dao;

	@Autowired
	private UsuarioRepository usuarioDao;
	
	@PersistenceContext
	private EntityManager em;

	public List<RendaDtoSaida> buscarRendas(Long idUsuario) {
		Stream<Renda> rendas = dao.findAll().stream();
		return rendas.sorted(Comparator.comparing(Renda::getDataRenda)).map(e -> {
			return new RendaDtoSaida(e);
		}).collect(Collectors.toList());
	}

	@Transactional
	public boolean salvar(@Valid RendaDtoEntrada dto) {
		try {
			Usuario usuario = usuarioDao.findById(dto.getIdUsuario()).get();
			Renda novaRenda = dto.criar(usuario);
			dao.saveAndFlush(novaRenda);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Transactional
	public List<RendaDtoSaida> buscarRendasDoMesSelecionaro(Long idUsuario, Integer mesSelecionado) {
		return dao.buscarRendasDoMesSelecionado(idUsuario, mesSelecionado).map(e -> {
			return new RendaDtoSaida(e);
		}).collect(Collectors.toList());
	}

	public Optional<RendaDtoSaida> buscarUnicaRenda(Long idRenda) {
		Optional<Renda> possivelRenda = dao.findById(idRenda);
		if (possivelRenda.isPresent()) {
			return Optional.of(new RendaDtoSaida(possivelRenda.get()));
		}
		return Optional.empty();
	}

	@Transactional
	public List<RendaDtoSaida> pesquisar(String descricao, String dataInicial, String dataFinal) {
		StringBuilder sql = new StringBuilder();
		sql.append("select r from Renda r ");
		definirParametros(sql, descricao, dataInicial, dataFinal);
		TypedQuery<Renda> query = em.createQuery(sql.toString(), Renda.class);
		definirValores(query, descricao, dataInicial, dataFinal);
		List<Renda> rendas = query.getResultList();
		return rendas.stream().sorted(Comparator.comparing(Renda::getDataRenda)).map(e -> {
			return new RendaDtoSaida(e);
		}).collect(Collectors.toList());
	}

	private void definirParametros(StringBuilder sql, String... campos) {
		sql.append(" where ");
		if (campos[0] != null || !campos[0].isEmpty()) {
			sql.append(" lower(r.descricao) like :descricao ");
		}
		if (campos[1] != null || !campos[1].isEmpty()) {
			if (sql.toString().contains("descricao")) {
				sql.append(" and r.dataRenda >= :dataInicial ");
			} else {
				sql.append(" r.dataRenda >= :dataInicial ");
			}
		}
		if (campos[2] != null || !campos[2].isEmpty()) {
			if (sql.toString().contains("descricao") || sql.toString().contains("dataRenda")) {
				sql.append(" and r.dataRenda <= :dataFinal ");
			} else {
				sql.append(" r.dataRenda <= :dataFinal ");
			}
		}
	}

	private void definirValores(Query q, String... campos) {
		if (campos[0] != null || !campos[0].isEmpty()) {
			q.setParameter("descricao", "%"+campos[0].toLowerCase()+"%");
		}
		if (campos[1] != null || !campos[1].isEmpty()) {
			q.setParameter("dataInicial", LocalDate.parse(campos[1]) );
		}
		if (campos[2] != null || !campos[2].isEmpty()) {
			q.setParameter("dataFinal", LocalDate.parse(campos[2]) );
		}
	}

}
