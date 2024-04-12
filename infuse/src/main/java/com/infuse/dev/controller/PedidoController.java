package com.infuse.dev.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.infuse.dev.dto.PedidoListDTO;
import com.infuse.dev.entity.Pedido;
import com.infuse.dev.exceptions.PedidoBusinessException;
import com.infuse.dev.service.PedidoService;

@RestController
@RequestMapping("v1/api/pedidos")
public class PedidoController {
	
	@Autowired
	private PedidoService pedidoService;

	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<String> createPedido(@RequestBody PedidoListDTO pedidos) {
		
		try {
			if(pedidos == null)
				throw new PedidoBusinessException("Requisição enviada sem dados");
			
			pedidoService.savePedidos(pedidos.getPedidos());
			
		}catch (PedidoBusinessException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping
	public ResponseEntity<List<Pedido>> consultaPedido(
			@RequestParam(value = "dataCadastro", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
			@RequestParam(value = "numeroControle", required = false) Integer numeroControle) {
		
		List<Pedido> pedidos;
		
		try {
			pedidos = pedidoService.consultarPedidos(date, numeroControle);
		}catch (PedidoBusinessException e) {
			 return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		return ResponseEntity.ok(pedidos);

	}

}