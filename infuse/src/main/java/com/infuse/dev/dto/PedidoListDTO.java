package com.infuse.dev.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.infuse.dev.entity.Pedido;

@XmlRootElement(name = "pedidoList")
public class PedidoListDTO {
	
	List<Pedido> pedidos;
	
	PedidoListDTO(){}

	@XmlElement(name = "pedido")
	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}
}