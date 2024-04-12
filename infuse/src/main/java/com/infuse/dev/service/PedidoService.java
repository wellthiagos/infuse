package com.infuse.dev.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infuse.dev.entity.Cliente;
import com.infuse.dev.entity.Pedido;
import com.infuse.dev.exceptions.PedidoBusinessException;
import com.infuse.dev.repository.PedidoRepository;

@Service
public class PedidoService {

	private static double FIVE_PERCENT = 0.05;
	private static double TEN_PERCENT = 0.1;

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private ClienteService clienteService;

	public List<Pedido> consultarPedidos(LocalDate dataCadastro, Integer numeroControle)
			throws PedidoBusinessException {
		List<Pedido> pedidos = new ArrayList<>();

		if (dataCadastro == null && numeroControle == null)
			pedidos = findAll();
		else if (dataCadastro != null && numeroControle != null)
			pedidos = findByNumeroControleAndDataCadastro(numeroControle, dataCadastro);
		else if (dataCadastro == null && numeroControle != null && numeroControle > 0)
			pedidos = findByNumeroControle(numeroControle);
		else if (numeroControle == null && dataCadastro != null)
			pedidos = findByDataCadastro(dataCadastro);

		if (pedidos.isEmpty())
			throw new PedidoBusinessException("dados não encontrados");

		return pedidos;
	}

	private List<Pedido> findAll() {
		List<Pedido> pedidos = pedidoRepository.findAll();
		return pedidos;
	}

	private List<Pedido> findByDataCadastro(LocalDate dataCadastro) {
		List<Pedido> pedidos = pedidoRepository.findByDataCadastro(dataCadastro);
		return pedidos;
	}

	private List<Pedido> findByNumeroControle(int numeroControle) {
		List<Pedido> pedidos = pedidoRepository.findByNumeroControle(numeroControle);
		return pedidos;
	}

	private List<Pedido> findByNumeroControleAndDataCadastro(int numeroControle, LocalDate dataCadastro) {
		List<Pedido> pedidos = pedidoRepository.findByNumeroControleAndDataCadastro(numeroControle, dataCadastro);
		return pedidos;
	}

	public void savePedidos(List<Pedido> pedidos) throws PedidoBusinessException {

		List<Pedido> pedidosCadastro = new ArrayList<>();

		if (pedidos.size() > 10)
			throw new PedidoBusinessException(
					String.format("O arquivo contém %s registros, o limite é 10", pedidos.size()));

		boolean isVerificaNumeroControleLista = possuiNumerosControleDuplicados(pedidos);

		if (isVerificaNumeroControleLista)
			throw new PedidoBusinessException("A lista enviada contém número de controle duplicado");

		for (Pedido pedido : pedidos) {
			validaPedido(pedido);
			ajustarDadosPedido(pedido);

			pedidosCadastro.add(pedido);
		}

		createPedidos(pedidosCadastro);

	}

	private void validaPedido(Pedido pedido) throws PedidoBusinessException {

		boolean existePedido = verificaExistePedido(pedido.getNumeroControle());

		if (existePedido)
			throw new PedidoBusinessException(
					String.format("O pedido: [%s] com o numero de controle: [%s] já foi cadastrado", pedido.toString(),
							pedido.getNumeroControle()));

		Optional<Cliente> cliente = clienteService.getClientById(pedido.getCliente().getId());

		if (cliente.isEmpty())
			throw new PedidoBusinessException(String.format("Cliente inxistente do pedido: [%s]", pedido.toString()));

		if (pedido.getValorUnitario() == 0) {
			throw new PedidoBusinessException(
					String.format("O pedido: [%s] foi enviado sem o valor unitário", pedido.toString()));
		} else if (pedido.getNomeProduto() == null || pedido.getNomeProduto().isEmpty()) {
			throw new PedidoBusinessException(
					String.format("O pedido: [%s] foi enviado sem o produto", pedido.toString()));
		}
	}

	private boolean verificaExistePedido(int numeroControle) {
		List<Pedido> pedidos = pedidoRepository.findByNumeroControle(numeroControle);
		boolean existe = !pedidos.isEmpty();
		return existe;
	}

	private void ajustarDadosPedido(Pedido pedido) {

		if (pedido.getDataCadastro() == null)
			pedido.setDataCadastro(LocalDate.now());

		if (pedido.getQuantidade() == 0)
			pedido.setQuantidade(1);

		if (pedido.getQuantidade() <= 5) {
			pedido.setValorTotal(calculaValorTotalSemDesconto(pedido));
		} else if (pedido.getQuantidade() > 5 && pedido.getQuantidade() < 10) {
			pedido.setValorTotal(calculaValorTotalComDesconto(pedido, FIVE_PERCENT));
		} else if (pedido.getQuantidade() >= 10) {
			pedido.setValorTotal(calculaValorTotalComDesconto(pedido, TEN_PERCENT));
		}
	}

	private double calculaValorTotalSemDesconto(Pedido pedido) {
		return (long) pedido.getValorUnitario() * pedido.getQuantidade();
	}

	private double calculaValorTotalComDesconto(Pedido pedido, double desconto) {
		double resultado = calculaValorTotalSemDesconto(pedido);
		double valorDesconto = resultado * desconto;
		double resultadoComDesconto = resultado - valorDesconto;
		return resultadoComDesconto;
	}

	@Transactional
	private void createPedidos(List<Pedido> pedidos) throws PedidoBusinessException {
		pedidos.forEach(pedidoRepository::save);
	}

	private boolean possuiNumerosControleDuplicados(List<Pedido> pedidos) {
		return pedidos.stream().map(Pedido::getNumeroControle).distinct().count() != pedidos.size();
	}
}