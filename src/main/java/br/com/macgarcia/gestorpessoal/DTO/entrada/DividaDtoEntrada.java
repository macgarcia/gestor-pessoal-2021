package br.com.macgarcia.gestorpessoal.DTO.entrada;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import br.com.macgarcia.gestorpessoal.model.Divida;
import br.com.macgarcia.gestorpessoal.model.Usuario;

public class DividaDtoEntrada implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "Descrição da divida é obrigatória.")
	private String descricao;

	@NotNull(message = "Valor da divida é obrigatório.")
	@Positive(message = "Informe um valor válido.")
	private Double valor;

	@JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy")
	@NotNull(message = "Data da divida é obrigatória.")
	private LocalDate dataDivida;

	@NotNull(message = "Condição da divida é obrigatório.")
	private boolean pago;

	@NotNull(message = "Identificador do usuário é origatório.")
	private Long idUsuario;

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

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Divida criar(Usuario usuario) {
		return new Divida(descricao, valor, dataDivida, pago, usuario);
	}

	public Divida atualizarDivida(Divida dividaExistente) {
		dividaExistente.setDescricao(descricao);
		dividaExistente.setValor(valor);
		dividaExistente.setDataDivida(dataDivida);
		dividaExistente.setPago(pago);
		return dividaExistente;
	}
}
