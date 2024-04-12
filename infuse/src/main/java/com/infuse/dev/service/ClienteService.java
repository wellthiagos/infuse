package com.infuse.dev.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infuse.dev.entity.Cliente;
import com.infuse.dev.repository.ClienteRepository;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	public Optional<Cliente> getClientById(Integer id) {
		Optional<Cliente> cliente = clienteRepository.findById(id);
		return cliente;
	}
}
