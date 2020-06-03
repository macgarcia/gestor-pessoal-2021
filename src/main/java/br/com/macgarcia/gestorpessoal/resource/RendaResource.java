package br.com.macgarcia.gestorpessoal.resource;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.macgarcia.gestorpessoal.DTO.entrada.RendaDtoEntrada;
import br.com.macgarcia.gestorpessoal.DTO.saida.RendaDtoSaida;
import br.com.macgarcia.gestorpessoal.service.RendaService;

@RestController
@RequestMapping(value = "/rendas")
@CrossOrigin
public class RendaResource {
	
	private RendaService service;
	
	@Autowired
	public RendaResource(RendaService service) {
		this.service = service;
	}	
	
	@GetMapping(value = "/{idUsuario}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> buscarTodasRendasDoUsuario(@PathVariable("idUsuario") Long idUsuario) {
		if (idUsuario <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Identificador inválido");
		}
		return ResponseEntity.status(HttpStatus.OK).body(service.buscarRendas(idUsuario));
	}
	
	@GetMapping(value = "/mesSelecionado/{idUsuario}/{mes}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> buscarRendasDoMesSelecionado(@PathVariable("idUsuario") Long idUsuario, @PathVariable("mes") Integer mesSelecionado) {
		if (idUsuario <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Identificador inválido");
		}
		return ResponseEntity.status(HttpStatus.OK).body(service.buscarRendasDoMesSelecionaro(idUsuario, mesSelecionado));
	}
	
	@GetMapping(value = "/unica/{idRenda}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> buscarUnicaRenda(@PathVariable("idRenda") Long idRenda) {
		if (idRenda <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		Optional<RendaDtoSaida> possivelRenda = service.buscarUnicaRenda(idRenda);
		if (!possivelRenda.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dados não encontrados");
		}
		return ResponseEntity.status(HttpStatus.OK).body(possivelRenda.get());
	}
	
	@GetMapping(value = "/pesquisar/{descricao}/{dataInicial}/{dataFinal}")
	public ResponseEntity<?> buscarRendasPorPesquisa(@PathVariable("descricao") String descricao, @PathVariable("dataInicial") String dataInicial
			, @PathVariable("dataFinal") String dataFinal) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(service.pesquisar(descricao, dataInicial, dataFinal));
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> salvarRenda(@Valid @RequestBody RendaDtoEntrada dto) {
		boolean salvo = service.salvar(dto);
		if (!salvo) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno");
		}
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
