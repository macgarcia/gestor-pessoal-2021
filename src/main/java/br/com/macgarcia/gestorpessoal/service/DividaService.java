package br.com.macgarcia.gestorpessoal.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.macgarcia.gestorpessoal.DTO.entrada.DividaDtoEntrada;
import br.com.macgarcia.gestorpessoal.DTO.saida.DividaDtoSaida;
import br.com.macgarcia.gestorpessoal.model.Divida;
import br.com.macgarcia.gestorpessoal.model.Usuario;
import br.com.macgarcia.gestorpessoal.repository.DividaRepository;
import br.com.macgarcia.gestorpessoal.repository.UsuarioRepository;

@Service
public class DividaService {
	
	@PersistenceContext
	private EntityManager em;
		
	private DividaRepository dao;
	private UsuarioRepository usuarioDao;
	private Validator validator;
	private String mensagemDeErro;
	
	private List<DividaDtoSaida> result;
	
	@Autowired
	public DividaService(DividaRepository dao, UsuarioRepository usuarioDao, Validator validator) {
		this.dao = dao;
		this.usuarioDao = usuarioDao;
		this.validator = validator;
	}
	
	//Método para validar as informações de entrada.
	public boolean validar(DividaDtoEntrada dto) {
		Set<ConstraintViolation<DividaDtoEntrada>> erros = validator.validate(dto);
		if (!erros.isEmpty()) {
			this.mensagemDeErro = erros.stream()
					.map(e -> e.getPropertyPath().toString().toUpperCase() + ": " + e.getMessageTemplate())
					.collect(Collectors.joining("\n"));
			return false;
		}
		return true;
	}

	public boolean verificarRegistro(Long idDivida) {
		return dao.existsById(idDivida);

	}

	public DividaDtoSaida buscarUnicaDivida(Long idDivida) {
		Divida divida = dao.findById(idDivida).get();
		return new DividaDtoSaida(divida);
	}

	@Transactional
	public boolean salvarDivida(@Valid DividaDtoEntrada dto) {
		try {
			Usuario usuario = usuarioDao.findById(dto.getIdUsuario()).get();
			Divida novaDivida = dto.criar(usuario);
			dao.saveAndFlush(novaDivida);
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	//Montagem do retorno para o cliente
	private void montarResultado(Page<Divida> list) {
		this.result = list.stream()
				.sorted(Comparator.comparing(Divida::getDataDivida))
				.map(e -> {return new DividaDtoSaida(e);})
				.collect(Collectors.toList());
	}

	@Transactional
	public Page<DividaDtoSaida> buscarTodasAsDividas(Long idUsuario, Pageable page) {
		var dividas = dao.buscarTodasAsDividasDoUsuario(idUsuario, page);
		montarResultado(dividas);
		return new PageImpl<DividaDtoSaida>(result, page, dividas.getTotalElements());
	}

	@Transactional
	public Page<DividaDtoSaida> buscarDividasDoMesSelecionado(Long idUsuario, Integer mes, Pageable page) {
		var dividas = dao.buscarDividasDoMesSelecionado(idUsuario, mes, page);
		montarResultado(dividas);
		return new PageImpl<DividaDtoSaida>(result, page, dividas.getTotalElements());
		
	}

	@Transactional
	public Page<DividaDtoSaida> pesquisarDividas(Long idUsuario,
												 String descricao,
												 String dataInicial,
												 String dataFinal,
												 Pageable page) {
		StringBuilder sql = new StringBuilder().
				append(" select d from Divida d where d.usuario.id = " + idUsuario);
		
		StringBuilder sqlCount = new StringBuilder()
				.append("select count(*) from Divida d where d.usuario.id = " + idUsuario);
		
		definirCondicao(sql, descricao, dataInicial, dataFinal);
		definirCondicao(sqlCount, descricao, dataInicial, dataFinal);
		
		TypedQuery<Divida> query 	= em.createQuery(sql.toString(), Divida.class);
		TypedQuery<Long> queryCount = em.createQuery(sqlCount.toString(), Long.class);
		
		definirValores(query, descricao, dataInicial, dataFinal);
		definirValores(queryCount, descricao, dataInicial, dataFinal);
		
		//Paginando a consulta.
		query.setFirstResult(page.getPageNumber() * page.getPageSize());
		query.setMaxResults(page.getPageSize());
		
		var totalDeRegistros = queryCount.getSingleResult();
		var dividas = query.getResultStream();
		
		var result = dividas
				.sorted(Comparator.comparing(Divida::getDataDivida))
				.map(e -> {return new DividaDtoSaida(e);})
				.collect(Collectors.toList());
		
		return new PageImpl<DividaDtoSaida>(result, page, totalDeRegistros);
	}
	
	private void definirCondicao(StringBuilder sql, String... campos) {
		if (!campos[0].equals("null") && !campos[0].isEmpty()) {
			sql.append(" and lower(d.descricao) like :descricao ");
		}
		if (!campos[1].equals("null") && !campos[1].isEmpty()) {
			sql.append(" and d.dataDivida >= :dataInicial ");
		}
		if (!campos[2].equals("null") && !campos[2].isEmpty()) {
			sql.append(" and d.dataDivida <= :dataFinal ");
		}
	}
	
	private void definirValores(Query q, String... campos) {
		if (!campos[0].equals("null") && !campos[0].isEmpty()) {
			q.setParameter("descricao", "%"+campos[0].toLowerCase()+"%");
		}
		if (!campos[1].equals("null") && !campos[1].isEmpty()) {
			q.setParameter("dataInicial", LocalDate.parse(campos[1]) );
		}
		if (!campos[2].equals("null") && !campos[2].isEmpty()) {
			q.setParameter("dataFinal", LocalDate.parse(campos[2]) );
		}
	}

	
	@Transactional
	public boolean atualizarDivida(Long idDivida, DividaDtoEntrada dto) {
		try {
			Divida dividaExistente = dao.findById(idDivida).get();
			Divida dividaAtualizada = dto.atualizarDivida(dividaExistente);
			dao.saveAndFlush(dividaAtualizada);
			return true;			
		} catch(Exception e) {
			return false;
		}
	}

	@Transactional
	public boolean excluirDivida(Long idDivida) {
		try {
			dao.deleteById(idDivida);
			return true;
		} catch(Exception e) {
			return false;
		}
	}

	//Utilizado para relatório mensal
	@Transactional
	public List<DividaDtoSaida> buscarInformacaoMensal(Long idUsuario, Integer mes, Integer ano) {
		Stream<Divida> dividas = dao.buscarInformacaoMensal(idUsuario, mes, ano);
		return dividas.sorted(Comparator.comparing(Divida::getDataDivida))
				.map(e -> {return new DividaDtoSaida(e);})
				.collect(Collectors.toList());
	}

	//Utilizado para relatório anual
	@Transactional
	public Map<Integer, List<DividaDtoSaida>> buscarDividasDoAno(Long idUsuario, Integer ano) {
		Stream<Divida> dividas = dao.buscarDividasDoAno(idUsuario, ano);
		return dividas.sorted(Comparator.comparing(Divida::getDataDivida))
				.map(e -> {return new DividaDtoSaida(e);})
				.collect(Collectors.groupingBy(DividaDtoSaida::getMesDaData));
	}
	
	public String getMensagemDeErro() {
		return mensagemDeErro;
	}
}
