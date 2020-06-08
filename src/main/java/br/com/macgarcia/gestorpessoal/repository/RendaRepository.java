package br.com.macgarcia.gestorpessoal.repository;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.macgarcia.gestorpessoal.model.Renda;

public interface RendaRepository extends JpaRepository<Renda, Long> {
	
	@Query("select r from Renda r where r.usuario.id = :idUsuario")
	Stream<Renda> buscarRendasDoUsuario(@Param("idUsuario") Long idUsuario);

	@Query("select r from Renda r where r.usuario.id = :idUsuario and month(r.dataRenda) = :mesSelecionado")
	Stream<Renda> buscarRendasDoMesSelecionado(@Param("idUsuario") Long idUsuario, @Param("mesSelecionado") Integer mesSelecionado);
	
	//Método de relatório mensal
	@Query("select r from Renda r where r.usuario.id = :idUsuario and month(r.dataRenda) = :mes and year(r.dataRenda) = :ano")
	Stream<Renda> buscarInformacaoMensal(@Param("idUsuario") Long idUsuario, @Param("mes") Integer mes, @Param("ano") Integer ano);
	
	//Método de relatório anual
	@Query("select r from Renda r where r.usuario.id = :idUsuario and year(r.dataRenda) = :ano")
	Stream<Renda> buscarRendasDoAno(@Param("idUsuario") Long idUsuario, @Param("ano") Integer ano);
}
