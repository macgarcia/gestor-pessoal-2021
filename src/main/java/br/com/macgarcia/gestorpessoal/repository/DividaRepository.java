package br.com.macgarcia.gestorpessoal.repository;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.macgarcia.gestorpessoal.model.Divida;

public interface DividaRepository extends JpaRepository<Divida, Long>{

	@Query("select d from Divida d where d.usuario.id = :idUsuario")
	Stream<Divida> buscarTodasAsDividasDoUsuario(@Param("idUsuario") Long idUsuario);
	
	@Query("select d from Divida d where d.usuario.id = :idUsuario and month(d.dataDivida) = :mesSelecionado")
	Stream<Divida> buscarDividasDoMesSelecionado(@Param("idUsuario") Long idUsuario, @Param("mesSelecionado") Integer mesSelecionado);
	
	//Método de relatório mensal
	@Query("select d from Divida d where d.usuario.id = :idUsuario and month(d.dataDivida) = :mes and year(d.dataDivida) = :ano")
	Stream<Divida> buscarInformacaoMensal(@Param("idUsuario") Long idUsuario, @Param("mes") Integer mes, @Param("ano") Integer ano);

	//Método de relatório anual
	@Query("select d from Divida d where d.usuario.id = :idUsuario and year(d.dataDivida) = :ano")
	Stream<Divida> buscarDividasDoAno(@Param("idUsuario") Long idUsuario, @Param("ano") Integer ano);
	
}
