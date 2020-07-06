package br.com.macgarcia.gestorpessoal.component;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.macgarcia.gestorpessoal.DTO.saida.InformacaoDtoSaida;
import br.com.macgarcia.gestorpessoal.model.Divida;
import br.com.macgarcia.gestorpessoal.model.Renda;
import br.com.macgarcia.gestorpessoal.repository.DividaRepository;
import br.com.macgarcia.gestorpessoal.repository.RendaRepository;

@Component
public class InformacaoComponent {

	private RendaRepository rendaDao;
	private DividaRepository dividaDao;
	
	@Autowired
	public InformacaoComponent(RendaRepository rendaDao, DividaRepository dividaDao) {
		this.rendaDao = rendaDao;
		this.dividaDao = dividaDao;
	}
	
	@Transactional
	public InformacaoDtoSaida gerarInformacoes(Long idUsuario, Integer mes) {
		var dividas = dividaDao.buscarDividas(idUsuario, mes);
		var rendas  = rendaDao.buscarRendas(idUsuario, mes);
		
		int numeroDeRendas = rendas.size();
		double valorTotalDeRendas = 0.0;
		for (Renda r : rendas) {
			valorTotalDeRendas += r.getValor();
		}
		
		int numeroDeDividas = dividas.size();
		double valorTotalDeDividas = 0.0;
		for (Divida d : dividas) {
			valorTotalDeDividas += d.getValor();
		}
		
		double saldoMensal = valorTotalDeRendas - valorTotalDeDividas;
		
		return new InformacaoDtoSaida(numeroDeRendas, valorTotalDeRendas, numeroDeDividas, valorTotalDeDividas, saldoMensal);

	}
	
	
}
