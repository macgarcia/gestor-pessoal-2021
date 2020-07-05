package br.com.macgarcia.gestorpessoal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.macgarcia.gestorpessoal.model.Anotacao;

public interface AnotacaoRepository extends JpaRepository<Anotacao, Long>{

	@Query("select a from Anotacao a where a.usuario.id = :idUsuario")
	Page<Anotacao> buscarAnotacoesPaginado(@Param("idUsuario") Long idUsuario, Pageable page);
}
