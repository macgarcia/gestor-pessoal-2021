package br.com.macgarcia.gestorpessoal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.macgarcia.gestorpessoal.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	@Query("select count(*) from Usuario u where u.email = :email")
	Integer findByEmail(@Param("email") String email);

	@Query("select count(*) from Usuario u where u.login = :login")
	Integer findByLogin(String login);

	Optional<Usuario> findByLoginAndSenha(String login, String senha);

}
