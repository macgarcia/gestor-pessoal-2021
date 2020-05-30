package br.com.macgarcia.gestorpessoal.resource;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.macgarcia.gestorpessoal.DTO.entrada.UsuarioDtoEntrada;
import br.com.macgarcia.gestorpessoal.DTO.saida.UsuarioDtoSaida;
import br.com.macgarcia.gestorpessoal.service.UsuarioService;
import br.com.macgarcia.gestorpessoal.validacao.impl.ValidarUnicidadeUsuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/usuarios")
@CrossOrigin
@Tag(name = "Serviços de manipulação de usuarios")
public class UsuarioResource {
	
	private UsuarioService service;
	
	@Autowired
	public UsuarioResource(UsuarioService service) {
		this.service = service;
	}
	
	@InitBinder
	public void iniciar(WebDataBinder web) {
		web.addValidators(new ValidarUnicidadeUsuario(service));
	}
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Buscar unico usuário pelo seu identificador(id)" ,description = "Recuperação do usuário pedido, serviço tem validações para evitar erros")
	@ApiResponse(responseCode = "200", description = "OK")
	@ApiResponse(responseCode = "400", description = "Caso o identificador for 0(ZERO) ou menor que 1")
	@ApiResponse(responseCode = "404", description = "Caso o usuário não for encontrado com o id")
	public ResponseEntity<?> buscarUsuarioPorId(@PathVariable("id") Long id) {
		if (id <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Identificador inválido");
		}
		if(service.verificar(id)) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(service.getUsuarioPorId(id));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body("Usuário não encontrado");
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Criação de um novo usuário" ,description = "Serviço para a inclusão de um novo usuário no sistema")
	@ApiResponse(responseCode = "201", description = "OK")
	public ResponseEntity<?> salvarUsuario(@RequestBody @Valid UsuarioDtoEntrada dto) {
		service.salvar(dto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Atualização de um usuário" ,description = "Serviço para a atualização de um usuário do sistema")
	@ApiResponse(responseCode = "200", description = "OK, usuário atualizado")
	@ApiResponse(responseCode = "302", description = "Caso o usuário já exista com as informações pedidas")
	@ApiResponse(responseCode = "400", description = "Caso o identificador for 0(ZERO) ou menor que 1")
	@ApiResponse(responseCode = "404", description = "Caso o usuário não for encontrado com o id")
	public ResponseEntity<?> atualizarUsuario(@PathVariable("id") Long id, @RequestBody UsuarioDtoEntrada dto) {
		if (id <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Identificador inválido");
		}
		if(service.verificar(id)) {
			boolean atualizou = service.atualizar(id, dto);
			if (!atualizou) {
				return ResponseEntity.status(HttpStatus.FOUND)
						.body("Já temos um cadastro com estas informações");
			}
			return ResponseEntity.status(HttpStatus.OK).build();
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body("Usuário não encontrado");
	}
	
	@GetMapping(value = "/login/{login}/{senha}")
	@Operation(summary = "Login no sistema" ,description = "Serviço para a operação de login no sistema")
	@ApiResponse(responseCode = "200", description = "OK, usuário validado")
	@ApiResponse(responseCode = "404", description = "Caso o usuário não for encontrado com o login ou senha")
	public ResponseEntity<?> logar(@PathVariable("login") String login, @PathVariable("senha") String senha) {
		Optional<UsuarioDtoSaida> possivelUsuario = service.logar(login, senha);
		if (possivelUsuario.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(possivelUsuario.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
	}

}
