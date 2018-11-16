package com.lucasaquila.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucasaquila.cursomc.domain.Estado;
import com.lucasaquila.cursomc.repositories.EstadoRepository;

@Service
public class EstadoService {
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	/**
	 * @return
	 */
	public List<Estado> findAllByOrderByNome() {
		return estadoRepository.findAllByOrderByNome();
	}

}
