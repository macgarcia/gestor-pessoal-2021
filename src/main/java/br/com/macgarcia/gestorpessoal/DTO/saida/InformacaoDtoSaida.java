package br.com.macgarcia.gestorpessoal.DTO.saida;

import java.io.Serializable;

public class InformacaoDtoSaida implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer numeroDeRendas;
	private Double valorTotalDeRendas;
	private Integer numeroDeDividas;
	private Double valorTotalDeDividas;
	private Double saldoMensal;

	public InformacaoDtoSaida(int numeroDeRendas, double valorTotalDeRendas, int numeroDeDividas,
			double valorTotalDeDividas, double saldoMensal) {
		this.numeroDeRendas = numeroDeRendas;
		this.valorTotalDeRendas = valorTotalDeRendas;
		this.numeroDeDividas = numeroDeDividas;
		this.valorTotalDeDividas = valorTotalDeDividas;
		this.saldoMensal = saldoMensal;
	}

	public Integer getNumeroDeRendas() {
		return numeroDeRendas;
	}

	public void setNumeroDeRendas(Integer numeroDeRendas) {
		this.numeroDeRendas = numeroDeRendas;
	}

	public Double getValorTotalDeRendas() {
		return valorTotalDeRendas;
	}

	public void setValorTotalDeRendas(Double valorTotalDeRendas) {
		this.valorTotalDeRendas = valorTotalDeRendas;
	}

	public Integer getNumeroDeDividas() {
		return numeroDeDividas;
	}

	public void setNumeroDeDividas(Integer numeroDeDividas) {
		this.numeroDeDividas = numeroDeDividas;
	}

	public Double getValorTotalDeDividas() {
		return valorTotalDeDividas;
	}

	public void setValorTotalDeDividas(Double valorTotalDeDividas) {
		this.valorTotalDeDividas = valorTotalDeDividas;
	}

	public Double getSaldoMensal() {
		return saldoMensal;
	}

	public void setSaldoMensal(Double saldoMensal) {
		this.saldoMensal = saldoMensal;
	}

}
