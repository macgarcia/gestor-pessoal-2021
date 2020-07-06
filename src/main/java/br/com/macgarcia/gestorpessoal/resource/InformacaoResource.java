package br.com.macgarcia.gestorpessoal.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.macgarcia.gestorpessoal.component.InformacaoComponent;

@RestController
@RequestMapping(value = "/informacoes")
@CrossOrigin
public class InformacaoResource {
	
	private InformacaoComponent component;

	@Autowired
	public InformacaoResource(InformacaoComponent component) {
		this.component = component;
	}
	
	@GetMapping(value = "/{idUsuario}/{mesSelecionado}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> buscarInformacoesMensais(@PathVariable("idUsuario") Long idUsuario, 
			@PathVariable("mesSelecionado") Integer mesSelecionado) {
		return ResponseEntity.status(HttpStatus.OK).body(component.gerarInformacoes(idUsuario, mesSelecionado));
	}

}
