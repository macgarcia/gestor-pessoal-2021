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
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.macgarcia.gestorpessoal.DTO.entrada.RendaDtoEntrada;
import br.com.macgarcia.gestorpessoal.DTO.saida.RendaDtoSaida;
import br.com.macgarcia.gestorpessoal.model.Renda;
import br.com.macgarcia.gestorpessoal.model.Usuario;
import br.com.macgarcia.gestorpessoal.repository.RendaRepository;
import br.com.macgarcia.gestorpessoal.repository.UsuarioRepository;

@Service
public class RendaService {

	private RendaRepository dao;
	private UsuarioRepository usuarioDao;
	private Validator validator;
	private String mensagemDeErro;
	
	private List<RendaDtoSaida> result;
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	public RendaService(RendaRepository dao, UsuarioRepository usuarioDao, Validator validator) {
		this.dao = dao;
		this.usuarioDao = usuarioDao;
		this.validator = validator;
	}
	
	//Método para validação do objeto de entrada
	public boolean validar(RendaDtoEntrada dto) {
		Set<ConstraintViolation<RendaDtoEntrada>> erros = validator.validate(dto);
		if (!erros.isEmpty()) {
			this.mensagemDeErro = erros.stream()
					.map(e -> e.getPropertyPath().toString().toUpperCase() + ": " + e.getMessageTemplate())
					.collect(Collectors.joining("\n"));
			return false;
		}
		return true;
	}
	
	//Método que monta o retorno para o cliente.
	private void montarRetorno(Page<Renda> list) {
		this.result = list.stream()
						  .sorted(Comparator.comparing(Renda::getDataRenda))
						  .map(e -> {return new RendaDtoSaida(e);})
						  .collect(Collectors.toList());
	}

	@Transactional
	public Page<RendaDtoSaida> buscarRendas(Long idUsuario, Pageable page) {
		var rendas = dao.buscarRendasDoUsuario(idUsuario, page);
		this.montarRetorno(rendas);
		return new PageImpl<RendaDtoSaida>(result, page, rendas.getTotalElements());
	}

	@Transactional
	public Page<RendaDtoSaida> buscarRendasDoMesSelecionaro(Long idUsuario, Integer mesSelecionado, Pageable page) {
		var rendas =  dao.buscarRendasDoMesSelecionado(idUsuario, mesSelecionado, page);
		this.montarRetorno(rendas);
		return new PageImpl<RendaDtoSaida>(result, page, rendas.getTotalElements());
	}
	
	@Transactional
	public boolean salvar(RendaDtoEntrada dto) {
		try {
			Usuario usuario = usuarioDao.findById(dto.getIdUsuario()).get();
			Renda novaRenda = dto.criar(usuario);
			dao.saveAndFlush(novaRenda);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public RendaDtoSaida buscarUnicaRenda(Long idRenda) {
		Renda renda =  dao.findById(idRenda).get();
		return new RendaDtoSaida(renda);
	}

	@Transactional
	public Page<RendaDtoSaida> pesquisar(Long idUsuario,
										 String descricao, 
										 String dataInicial, 
										 String dataFinal, 
										 Pageable page) {

		StringBuilder sql = new StringBuilder()
				.append("select r from Renda r where r.usuario.id = " + idUsuario);
		
		StringBuilder sqlCount = new StringBuilder()
				.append("select count(*) from Renda r where r.usuario.id = " + idUsuario);
		
		definirParametros(sql, descricao, dataInicial, dataFinal);
		definirParametros(sqlCount, descricao, dataInicial, dataFinal);
		
		TypedQuery<Renda> query = em.createQuery(sql.toString(), Renda.class);
		TypedQuery<Long> queryCount = em.createQuery(sqlCount.toString(), Long.class);
		
		definirValores(query, descricao, dataInicial, dataFinal);
		definirValores(queryCount, descricao, dataInicial, dataFinal);
		
		//Paginando a consulta.
		query.setFirstResult(page.getPageNumber() * page.getPageSize());
		query.setMaxResults(page.getPageSize());
		
		var totalDeRegistros = queryCount.getSingleResult();
		var rendas = query.getResultStream();
		
		var collect = rendas
				.sorted(Comparator.comparing(Renda::getDataRenda))
				.map(e -> {return new RendaDtoSaida(e);})
				.collect(Collectors.toList());
		 
		return new PageImpl<RendaDtoSaida>(collect, page, totalDeRegistros);
	}

	private void definirParametros(StringBuilder sql, String... campos) {
		if (!campos[0].equals("null") && !campos[0].isEmpty()) {
			sql.append(" and lower(r.descricao) like :descricao ");
		}
		if (!campos[1].equals("null") && !campos[1].isEmpty()) {
			sql.append(" and r.dataRenda >= :dataInicial ");
		}
		if (!campos[2].equals("null") && !campos[2].isEmpty()) {
			sql.append(" and r.dataRenda <= :dataFinal ");
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
	public boolean atualizarRenda(Long idRenda, RendaDtoEntrada dto) {
		try {
			Renda rendaExistente = dao.findById(idRenda).get();
			Renda rendaAtualizada = dto.atualizar(rendaExistente);
			dao.saveAndFlush(rendaAtualizada);
			return true;
		} catch (Exception e) {
			return false;
		}		
	}

	@Transactional
	public boolean excluirRenda(Long idRenda) {
		try {
			dao.deleteById(idRenda);
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	//Verifica a existencia do registro
	public boolean verificarExistencia(Long id) {
		return dao.existsById(id);
	}

	//Utilizado para relatório mensal
	@Transactional
	public List<RendaDtoSaida> buscarInformacaoMensal(Long idUsuario, Integer mes, Integer ano) {
		Stream<Renda> rendas = dao.buscarInformacaoMensal(idUsuario, mes, ano);
		return rendas.sorted(Comparator.comparing(Renda::getDataRenda))
				.map(e -> {return new RendaDtoSaida(e);})
				.collect(Collectors.toList());
	}

	//Utilizado para relatório anual
	@Transactional
	public Map<Integer, List<RendaDtoSaida>> buscarRendasDoAno(Long idUsuario, Integer ano) {
		Stream<Renda> rendas = dao.buscarRendasDoAno(idUsuario, ano);
		return rendas.sorted(Comparator.comparing(Renda::getDataRenda))
				.map(e -> {return new RendaDtoSaida(e);})
				.collect(Collectors.groupingBy(RendaDtoSaida::getMesDaData));
	}

	public String getMensagemDeErro() {
		return mensagemDeErro;
	}

}
