package br.com.macgarcia.gestorpessoal.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

import br.com.macgarcia.gestorpessoal.DTO.entrada.DividaDtoEntrada;
import br.com.macgarcia.gestorpessoal.service.DividaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/dividas")
@CrossOrigin
@Tag(name = "Serviços para manipulação de dívidas do usuário")
public class DividaResource {
	
	private DividaService service;
	
	@Autowired
	public DividaResource(DividaService service) {
		this.service = service;
	}
	
	@Operation(summary = "Salvar uma nova dívida", description = "A partir dos dados que estiverem no corpo da requisição, será criado o novo registro")
	@ApiResponse(responseCode = "200", description = "Registro criado com sucesso")
	@ApiResponse(responseCode = "400", description = "Dados inconsistentes")
	@ApiResponse(responseCode = "500", description = "Erro interno ao tentar salvar os dados")
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> criarNovaDivida(@RequestBody DividaDtoEntrada dto) {
		boolean validou = service.validar(dto);
		if (!validou) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(service.getMensagemDeErro());
		}
		
 		boolean salvo = service.salvarDivida(dto);
		if (!salvo) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar");
		}
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@Operation(summary = "Buscar única dívida do usuário", description = "Através do identificador é retornada a dívida")
	@ApiResponse(responseCode = "200", description = "Recuperou as informações solicitadas")
	@ApiResponse(responseCode = "400", description = "Identificador do usuário inválido")
	@ApiResponse(responseCode = "404", description = "Dados incexistentes")
	@GetMapping(value = "/unica/{idDivida}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> buscarUnicaDivida(@PathVariable("idDivida") Long idDivida) {
		if (idDivida <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Identificador inválido");
		}
		boolean existe = service.verificarRegistro(idDivida);
		if (!existe) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dados inexistentes");
		}
		return ResponseEntity.status(HttpStatus.OK).body(service.buscarUnicaDivida(idDivida));
	}
	
	@Operation(summary = "Buscar todas as dívidas do usuário", description = "Através do identificador é retornada as dívidas do usuário")
	@ApiResponse(responseCode = "200", description = "Recuperou as informações solicitadas")
	@ApiResponse(responseCode = "400", description = "Identificador do usuário inválido")
	@GetMapping(value = "/{idUsuario}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> buscarDividasDoUsuario(@PathVariable("idUsuario") Long idUsuario, 
													@PageableDefault(page = 0, size = 5) Pageable page) {
		if (idUsuario <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Identificador inválido");
		}
		return ResponseEntity.status(HttpStatus.OK).body(service.buscarTodasAsDividas(idUsuario, page));
	}
	
	@Operation(summary = "Buscar todas as dívidas do usuário de um mês", description = "Através do identificador é retornada as dívidas do usuário")
	@ApiResponse(responseCode = "200", description = "Recuperou as informações solicitadas")
	@ApiResponse(responseCode = "400", description = "Identificador inválido")
	@GetMapping(value = "/mesSelecionado/{idUsuario}/{mesSelecionado}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> buscarDividasDoMesSelecionado(@PathVariable("idUsuario") Long idUsuario, 
														   @PathVariable("mesSelecionado") Integer mes, 
														   @PageableDefault(page = 0, size = 5) Pageable page) {
		if (idUsuario <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Identificador inválido");
		}
		if (mes > 12 || mes <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mês informado inválido");
		}
		return ResponseEntity.status(HttpStatus.OK).body(service.buscarDividasDoMesSelecionado(idUsuario, mes, page));
	}
	
	@Operation(summary = "Pesquisa de dívidas", 
			description = "Através das informações: Descrição e um periodo com data inicial e data final, será retornada as dívidas que atenderem os parâmetros."
			+ " Caso queira não utilizar algum dos parâmetros, passe a palavra 'null' para o mesmo."
			+" Informe a data no formato: yyyy-MM-dd")
	@ApiResponse(responseCode = "200", description = "Requisição feita com sucesso")
	@ApiResponse(responseCode = "400", description = "Identificador inválido")
	@GetMapping(value = "/pesquisar/{idUsuario}/{descricao}/{dataInicial}/{dataFinal}")
	public ResponseEntity<?> pesuisarDividas(@PathVariable("idUsuario") Long idUsuario,
											 @PathVariable("descricao") String descricao,
											 @PathVariable("dataInicial") String dataInicial,
											 @PathVariable("dataFinal") String dataFinal,
											 @PageableDefault(page = 0, size = 5) Pageable page) {
		if (idUsuario <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Identificador inválido");
		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(service.pesquisarDividas(idUsuario, descricao, dataInicial, dataFinal, page));
	}

	@Operation(summary = "Atualização de uma dívida", description = "Através do identificador a divida será atualizada com os dados da requisição")
	@ApiResponse(responseCode = "200", description = "Requisição feita com sucesso")
	@ApiResponse(responseCode = "400", description = "Identificador inválido")
	@ApiResponse(responseCode = "500", description = "Erro interno ao tentar atualizar")
	@PutMapping(value = "/{idDivida}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> atualizarDivida(@PathVariable("idDivida") Long idDivida, @RequestBody DividaDtoEntrada dto) {
		if (idDivida <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Identificador inválido");
		}
		boolean atualizou = service.atualizarDivida(idDivida, dto);
		if (!atualizou) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar");
		}
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@Operation(summary = "Exclusão de uma dívida", description = "Através do identificador a divida a mesma será excluída")
	@ApiResponse(responseCode = "200", description = "Requisição feita com sucesso")
	@ApiResponse(responseCode = "400", description = "Identificador inválido")
	@ApiResponse(responseCode = "404", description = "Dados inexistentes")
	@ApiResponse(responseCode = "500", description = "Erro interno ao tentar excluir")
	@DeleteMapping(value = "/{idDivida}")
	public ResponseEntity<?> excluirDivida(@PathVariable("idDivida") Long idDivida) {
		if (idDivida <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Identificador inválido");
		}
		boolean existe = service.verificarRegistro(idDivida);
		if (!existe) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dados inexistentes");
		}
		boolean excluiu = service.excluirDivida(idDivida);
		if (!excluiu) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao tentar excluir");
		}
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	//Método para relatório mensal
	@Operation(summary = "Serviço para relatórios mensais", description = "Geração de relatório mensal")
	@ApiResponse(responseCode = "200", description = "Requisição feita com sucesso")
	@ApiResponse(responseCode = "400", description = "Identificador inválido")
	@GetMapping(value = "/relatorio/{idUsuario}/{mes}/{ano}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> gerarRelatotioMensal(@PathVariable("idUsuario") Long idUsuario,
												  @PathVariable("mes") Integer mes,
												  @PathVariable("ano") Integer ano) {
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
	public ResponseEntity<?> buscarDividasDoAno(@PathVariable("idUsuario") Long idUsuario, @PathVariable("ano") Integer ano) {
		if (idUsuario <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Identificador inválido");
		}
		if (ano.toString().length() != 4) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Identificador do ano inválido");
		}
		return ResponseEntity.status(HttpStatus.OK).body(service.buscarDividasDoAno(idUsuario, ano));
	}
	
	
}
