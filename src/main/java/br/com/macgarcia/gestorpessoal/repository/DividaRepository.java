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
}
