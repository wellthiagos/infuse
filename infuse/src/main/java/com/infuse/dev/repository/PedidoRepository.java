package com.infuse.dev.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.infuse.dev.entity.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer>{

	public List<Pedido> findByDataCadastro(LocalDate dataCadastro);
	
	public List<Pedido> findByNumeroControle(int numeroControle);
	
	public List<Pedido> findByNumeroControleAndDataCadastro(Integer numeroControle, LocalDate dataCadastro);

}
