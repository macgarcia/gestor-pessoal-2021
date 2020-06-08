package br.com.macgarcia.gestorpessoal.DTO.saida;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import br.com.macgarcia.gestorpessoal.model.Renda;

public class RendaDtoSaida implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@NotBlank(message = "Decrição da renda é obrigatório")
	private String descricao;
	
	@NotNull(message = "Valor da renda é obrigatório")
	@Positive(message = "Valor informado é inválido")
	private Double valor;
	
	@NotNull(message = "Data da renda é obrigatória")
	@JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy")
	private LocalDate dataRenda;

	@Deprecated
	public RendaDtoSaida() {
	}

	public RendaDtoSaida(Renda e) {
		this.id = e.getId();
		this.descricao = e.getDescricao();
		this.valor = e.getValor();
		this.dataRenda = e.getDataRenda();
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

	public LocalDate getDataRenda() {
		return dataRenda;
	}

	public void setDataRenda(LocalDate dataRenda) {
		this.dataRenda = dataRenda;
	}
	
	@JsonIgnore
	public Integer getMesDaData() {
		return this.dataRenda.getMonth().getValue();
	}
}
