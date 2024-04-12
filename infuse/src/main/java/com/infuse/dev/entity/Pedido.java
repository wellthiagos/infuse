package com.infuse.dev.entity;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pedido")
public class Pedido {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "numero_controle", unique = true)
	private int numeroControle;
	
	@Column(name = "nome_produto")
	private String nomeProduto;
	
	@Column(name = "valor_unitario")
	private double valorUnitario;
	
	@Column(name = "quantidade")
	private int quantidade;
	
	@Column(name = "valor_total")
	private double valorTotal;
	
	@Column(name = "data_cadastro")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate dataCadastro;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cliente_id", referencedColumnName = "id" )
	private Cliente cliente;
	
	public Pedido() { }

	public Pedido(Integer id, int numeroControle, String nomeProduto, double valorUnitario, int quantidade,
			double valorTotal, LocalDate dataCadastro, Cliente cliente) {
		this.id = id;
		this.numeroControle = numeroControle;
		this.nomeProduto = nomeProduto;
		this.valorUnitario = valorUnitario;
		this.quantidade = quantidade;
		this.valorTotal = valorTotal;
		this.dataCadastro = dataCadastro;
		this.cliente = cliente;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getNumeroControle() {
		return numeroControle;
	}

	public void setNumeroControle(int numeroControle) {
		this.numeroControle = numeroControle;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public double getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(double valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public LocalDate getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(LocalDate dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Override
	public String toString() {
		return "Pedido [id=" + id + ", numeroControle=" + numeroControle + ", nomeProduto=" + nomeProduto
				+ ", valorUnitario=" + valorUnitario + ", quantidade=" + quantidade + ", valorTotal=" + valorTotal
				+ ", dataCadastro=" + dataCadastro + ", cliente=" + cliente + "]";
	}
}