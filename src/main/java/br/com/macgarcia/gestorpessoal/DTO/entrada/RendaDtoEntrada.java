package br.com.macgarcia.gestorpessoal.DTO.entrada;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import br.com.macgarcia.gestorpessoal.model.Renda;
import br.com.macgarcia.gestorpessoal.model.Usuario;

public class RendaDtoEntrada implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@NotBlank(message = "Decrição da renda é obrigatório")
	private String descricao;
	
	@NotNull(message = "Valor da renda é obrigatório")
	@Positive(message = "Valor informado é inválido")
	private Double valor;
	
	@NotNull(message = "Data da renda é obrigatória")
	@JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy")
	private LocalDate dataRenda;
	
	@NotNull(message = "Identificador do usuário é obrigatório")
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

	public LocalDate getDataRenda() {
		return dataRenda;
	}

	public void setDataRenda(LocalDate dataRenda) {
		this.dataRenda = dataRenda;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Renda criar(Usuario usuario) {
		return new Renda(this.descricao, this.valor, this.dataRenda, usuario);
	}

	public Renda atualizar(Renda rendaExistente) {
		rendaExistente.setDescricao(descricao);
		rendaExistente.setValor(valor);
		rendaExistente.setDataRenda(dataRenda);
		return rendaExistente;
	}
}
