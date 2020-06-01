package br.com.macgarcia.gestorpessoal.resource;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.macgarcia.gestorpessoal.DTO.entrada.AnotacaoDtoEntrada;
import br.com.macgarcia.gestorpessoal.DTO.saida.AnotacaoDtoSaida;
import br.com.macgarcia.gestorpessoal.service.AnotacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/anotacao")
@CrossOrigin
@Tag(name = "Serviços de manipulação de anotações")
public class AnotacaoResource {
	
	private AnotacaoService service;
	
	@Autowired
	public AnotacaoResource(AnotacaoService service) {
		this.service = service;
	}

	@Operation(summary = "Buscar unica as anotação do usuário", description = "Através do identificador é retornada a anotação")
	@ApiResponse(responseCode = "200", description = "Recuperou as informações solicitadas")
	@ApiResponse(responseCode = "400", description = "Identificador do usuário inválido")
	@ApiResponse(responseCode = "404", description = "Dados não encontrados")
	@GetMapping(value = "/unica/{id}")
	public ResponseEntity<?> buscarUnicaAnotacao(@PathVariable("id") Long id) {
		if (id <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Identificador inválido");
		}
		Optional<AnotacaoDtoSaida> possivelAnotacao = service.buscarUnica(id);
		if (!possivelAnotacao.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dados não encontrados");
		}
		return ResponseEntity.status(HttpStatus.OK).body(possivelAnotacao.get());
	}

	@Operation(summary = "Buscar todas as anotações do usuário", description = "Através do identificador do usuário é retornado todas as suas anotações")
	@ApiResponse(responseCode = "200", description = "Recuperou as informações solicitadas")
	@ApiResponse(responseCode = "400", description = "Identificador do usuário inválido")
	@GetMapping(value = "/{idUsuario}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> buscarAnotacoesDoUsuario(@PathVariable("idUsuario") Long idUsuario) {
		if (idUsuario <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Identificador inválido");
		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(service.buscarAnotacoes());
	}
	
	@Operation(summary = "Criação de uma nova anotação", description = "Criação de uma nova anotação baseado json que esta no corpo da requisição")
	@ApiResponse(responseCode = "201", description = "Anotação criada")
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> salvarAnotacao(@RequestBody AnotacaoDtoEntrada dto) {
		service.salvarAnotacao(dto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@Operation(summary = "Atualização de uma anotação", description = "Atualização feita através do identificador da anotação e os dados enviados contidos no json")
	@ApiResponse(responseCode = "200", description = "Anotação atualizada")
	@ApiResponse(responseCode = "400", description = "Identificador da anotação inválido")
	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> atualizarAnotacao(@PathVariable("id") Long id, @RequestBody AnotacaoDtoEntrada dto) {
		if (id <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Identificador inválido");
		}
		service.atualizarAnotacao(id, dto);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@Operation(summary = "Exclusão de uma anotação", description = "Exclusão através do identificador enviado")
	@ApiResponse(responseCode = "200", description = "Exclusão concluída")
	@ApiResponse(responseCode = "400", description = "Identificador da anotação inválido")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> apagarAnotacao(@PathVariable("id") Long idAnotacao) {
		if (idAnotacao <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Identificador inválido");
		}
		service.apagarAnotacao(idAnotacao);
		return ResponseEntity.status(HttpStatus.OK).build();
	}	
}
