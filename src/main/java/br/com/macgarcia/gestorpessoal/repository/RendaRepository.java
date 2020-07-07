package br.com.macgarcia.gestorpessoal.repository;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.macgarcia.gestorpessoal.model.Renda;

public interface RendaRepository extends JpaRepository<Renda, Long> {
	
	@Query("select r from Renda r where r.usuario.id = :idUsuario")
	Page<Renda> buscarRendasDoUsuario(@Param("idUsuario") Long idUsuario, Pageable page);

	@Query("select r from Renda r where r.usuario.id = :idUsuario and month(r.dataRenda) = :mesSelecionado")
	Page<Renda> buscarRendasDoMesSelecionado(@Param("idUsuario") Long idUsuario, @Param("mesSelecionado") Integer mesSelecionado, Pageable page);
	
	//Método de relatório mensal
	@Query("select r from Renda r where r.usuario.id = :idUsuario and month(r.dataRenda) = :mes and year(r.dataRenda) = :ano")
	Stream<Renda> buscarInformacaoMensal(@Param("idUsuario") Long idUsuario, @Param("mes") Integer mes, @Param("ano") Integer ano);
	
	//Método de relatório anual
	@Query("select r from Renda r where r.usuario.id = :idUsuario and year(r.dataRenda) = :ano")
	Stream<Renda> buscarRendasDoAno(@Param("idUsuario") Long idUsuario, @Param("ano") Integer ano);
	
	//Método para enviar as informações mensais
	@Query("select r from Renda r where r.usuario.id = :idUsuario and month(r.dataRenda) = :mesSelecionado")
	List<Renda> buscarRendas(@Param("idUsuario") Long idUsuario, @Param("mesSelecionado") Integer mesSelecionado);
}
