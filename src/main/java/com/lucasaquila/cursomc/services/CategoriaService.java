package com.lucasaquila.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucasaquila.cursomc.domain.Categoria;
import com.lucasaquila.cursomc.exceptions.ObjectNotFoundException;
import com.lucasaquila.cursomc.repositories.CategoriaRepository;

@Service
public class CategoriaService {
	
	/**
	 * 
	 */
	@Autowired
	private CategoriaRepository categoriaRepository;

	public Categoria buscar(Integer id) {
		Optional<Categoria> obj = categoriaRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
		"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
		}
	
	/**
	 * 
	 * @param obj
	 * @return
	 */
	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return categoriaRepository.save(obj);
	}
}
