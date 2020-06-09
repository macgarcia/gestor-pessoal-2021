package br.com.macgarcia.gestorpessoal.resource;

import javax.validation.Valid;

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

import br.com.macgarcia.gestorpessoal.DTO.entrada.RendaDtoEntrada;
import br.com.macgarcia.gestorpessoal.service.RendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/rendas")
@CrossOrigin
@Tag(name = "Serviços para manipulação de rendas do usuário")
public class RendaResource {
	
	private RendaService service;
	
	@Autowired
	public RendaResource(RendaService service) {
		this.service = service;
	}
	
	@Operation(summary = "Buscar todas as rendas do usuário", description = "Através do identificador é retornada a renda")
	@ApiResponse(responseCode = "200", description = "Recuperou as informações solicitadas")
	@ApiResponse(responseCode = "400", description = "Identificador do usuário inválido")
	@GetMapping(value = "/{idUsuario}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> buscarTodasRendasDoUsuario(@PathVariable("idUsuario") Long idUsuario) {
		if (idUsuario <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Identificador inválido");
		}
		return ResponseEntity.status(HttpStatus.OK).body(service.buscarRendas(idUsuario));
	}
	
	@Operation(summary = "Buscar todas as rendas de um mês", description = "Através do identificador e o mês selecionado, é retornada a lista de rendas")
	@ApiResponse(responseCode = "200", description = "Recuperou as informações solicitadas")
	@ApiResponse(responseCode = "400", description = "Identificadores inválido")
	@GetMapping(value = "/mesSelecionado/{idUsuario}/{mes}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> buscarRendasDoMesSelecionado(@PathVariable("idUsuario") Long idUsuario, @PathVariable("mes") Integer mesSelecionado) {
		if (idUsuario <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Identificador inválido");
		}
		if ( mesSelecionado > 12 || mesSelecionado <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Indicador do mês inválido");
		}
		return ResponseEntity.status(HttpStatus.OK).body(service.buscarRendasDoMesSelecionaro(idUsuario, mesSelecionado));
	}
	
	@Operation(summary = "Buscar unica renda do usuário", description = "Através do identificador é retornada a informação especifica")
	@ApiResponse(responseCode = "200", description = "Recuperou as informações solicitadas")
	@ApiResponse(responseCode = "400", description = "Identificadores inválido")
	@ApiResponse(responseCode = "404", description = "Dados não encontrados")
	@GetMapping(value = "/unica/{idRenda}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> buscarUnicaRenda(@PathVariable("idRenda") Long idRenda) {
		if (idRenda <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		boolean existe = service.verificarExistencia(idRenda);
		if (!existe) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dados não encontrados");
		}
		return ResponseEntity.status(HttpStatus.OK).body(service.buscarUnicaRenda(idRenda));
	}
	
	@Operation(summary = "Pesquisa de rendas", 
			description = "Através das informações: Descrição e um periodo com data inicial e data final, será retornada as rendas que atenderem os parâmetros."
			+ " Caso queira não utilizar algum dos parâmetros, passe a palavra 'null' para o mesmo."
			+" Informe a data no formato: yyyy-MM-dd")
	@ApiResponse(responseCode = "200", description = "Requisição feita com sucesso")
	@ApiResponse(responseCode = "400", description = "Identificadores inválido")
	@GetMapping(value = "/pesquisar/{idUsuario}/{descricao}/{dataInicial}/{dataFinal}")
	public ResponseEntity<?> buscarRendasPorPesquisa(@PathVariable("idUsuario") Long idUsuario,
			@PathVariable("descricao") String descricao, 
			@PathVariable("dataInicial") String dataInicial,
			@PathVariable("dataFinal") String dataFinal) {
		if (idUsuario <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(service.pesquisar(idUsuario, descricao, dataInicial, dataFinal));
	}

	@Operation(summary = "Serviço para adição de uma nova renda", description = "Adição de uma nova renda a partir dos dados embutidos na requisição")
	@ApiResponse(responseCode = "200", description = "Requisição feita com sucesso")
	@ApiResponse(responseCode = "500", description = "Erro ao salvar")
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> salvarRenda(@Valid @RequestBody RendaDtoEntrada dto) {
		boolean salvo = service.salvar(dto);
		if (!salvo) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno");
		}
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@Operation(summary = "Serviço para atualização de uma nova renda", description = "Atualização de uma renda")
	@ApiResponse(responseCode = "200", description = "Atualização realizada com sucesso")
	@ApiResponse(responseCode = "400", description = "Identificador inválido")
	@ApiResponse(responseCode = "404", description = "Dados indexistentes")
	@ApiResponse(responseCode = "500", description = "Erro na atualização")
	@PutMapping(value = "/{idRenda}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> atualizarRenda(@PathVariable("idRenda") Long idRenda, @RequestBody RendaDtoEntrada dto) {
		if (idRenda <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
		boolean existe = service.verificarExistencia(idRenda);
		if (!existe) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dados innexistentes");
		}
		
		boolean atualizaou = service.atualizarRenda(idRenda, dto);
		if (!atualizaou) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar a renda");
		}
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@Operation(summary = "Serviço para exclusão de uma renda", description = "Exclusão de uma renda")
	@ApiResponse(responseCode = "200", description = "Exclusão realizada com sucesso")
	@ApiResponse(responseCode = "400", description = "Identificador inválido")
	@ApiResponse(responseCode = "404", description = "Dados indexistentes")
	@ApiResponse(responseCode = "500", description = "Erro na atualização")
	@DeleteMapping(value = "/{idRenda}")
	public ResponseEntity<?> excluirRenda(@PathVariable("idRenda") Long idRenda) {
		if (idRenda <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		boolean existe = service.verificarExistencia(idRenda);
		if (!existe) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dados inexistentes");
		}
		boolean excluiu = service.excluirRenda(idRenda);
		if (!excluiu) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao excluir");
		}
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	//Método para relatório
	@Operation(summary = "Serviço para relatórios", description = "Geração de relatório mensal")
	@ApiResponse(responseCode = "200", description = "Requisição feita com sucesso")
	@ApiResponse(responseCode = "400", description = "Identificador inválido")
	@GetMapping(value = "/relatorio/{idUsuario}/{mes}/{ano}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> gerarRelatorioMensal(@PathVariable("idUsuario") Long idUsuario,
			@PathVariable("mes") Integer mes, @PathVariable("ano") Integer ano) {
		if (idUsuario <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Identificador inválido");
		}
		if (mes > 12 || mes <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Identificador do mês inválido");
		}
		if (ano.toString().length() != 4) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Identificador do ano inválido");
		}
		return ResponseEntity.status(HttpStatus.OK).body(service.buscarInformacaoMensal(idUsuario, mes, ano));
	}
	
	//Método para relatório anual
	@Operation(summary = "Serviço para relatórios anuais", description = "Geração de relatório mensal")
	@ApiResponse(responseCode = "200", description = "Requisição feita com sucesso")
	@ApiResponse(responseCode = "400", description = "Identificador inválido")
	@GetMapping(value = "/relatorio/{idUsuario}/{ano}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> buscarRendasDoAno(@PathVariable("idUsuario") Long idUsuario, @PathVariable("ano") Integer ano) {
		if (idUsuario <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Identificador inválido");
		}
		if (ano.toString().length() != 4) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Identificador do ano inválido");
		}
		return ResponseEntity.status(HttpStatus.OK).body(service.buscarRendasDoAno(idUsuario, ano));
	}
}
