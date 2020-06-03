package br.com.macgarcia.gestorpessoal.repository;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.macgarcia.gestorpessoal.model.Renda;

public interface RendaRepository extends JpaRepository<Renda, Long> {

	@Query("select r from Renda r where r.usuario.id = :idUsuario and month(r.dataRenda) = :mesSelecionado")
	Stream<Renda> buscarRendasDoMesSelecionado(@Param("idUsuario") Long idUsuario, @Param("mesSelecionado") Integer mesSelecionado);
}
