package br.com.macgarcia.gestorpessoal.DTO.saida;

import java.io.Serializable;
import java.time.LocalDate;

import br.com.macgarcia.gestorpessoal.model.Divida;

public class DividaDtoSaida implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String descricao;
	private Double valor;
	private LocalDate dataDivida;
	private boolean pago;

	public DividaDtoSaida(Divida divida) {
		this.id = divida.getId();
		this.descricao = divida.getDescricao();
		this.valor = divida.getValor();
		this.dataDivida = divida.getDataDivida();
		this.pago = divida.isPago();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public LocalDate getDataDivida() {
		return dataDivida;
	}

	public void setDataDivida(LocalDate dataDivida) {
		this.dataDivida = dataDivida;
	}

	public boolean isPago() {
		return pago;
	}

	public void setPago(boolean pago) {
		this.pago = pago;
	}
}
