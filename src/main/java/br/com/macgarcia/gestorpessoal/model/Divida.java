package br.com.macgarcia.gestorpessoal.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "DIVIDA")
public class Divida extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "Descrição da divida é obrigatória")
	@Column(name = "DESCRICAO")
	private String descricao;
	
	@NotNull(message = "Valor da divida é obrigatória")
	@Positive(message = "O valor informado é inválido")
	@Column(name = "VALOR")
	private Double valor;
	
	@NotNull(message = "Data da divida é obrigatória")
	@Column(name = "DATA_DIVIDA")
	private LocalDate dataDivida;
	
	@NotNull(message = "Condição da divida é obrigatória")
	@Column(name = "PAGO")	
	private boolean pago;
	
	@JsonIgnore
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario usuario;
	
	@Deprecated
	public Divida() {}

	public Divida(@NotBlank(message = "Descrição da divida é obrigatória") String descricao,
			@NotNull(message = "Valor da divida é obrigatório") @Positive(message = "Informe um valor válido") Double valor,
			@NotNull(message = "Data da divida é obrigatória") LocalDate dataDivida,
			@NotNull(message = "Condição da divida é obrigatório") boolean pago, Usuario usuario) {
		this.descricao = descricao;
		this.valor = valor;
		this.dataDivida = dataDivida;
		this.pago = pago;
		this.usuario = usuario;
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}	
}
