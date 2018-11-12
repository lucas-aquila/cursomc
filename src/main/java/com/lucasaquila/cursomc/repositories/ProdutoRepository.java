package com.lucasaquila.cursomc.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lucasaquila.cursomc.domain.Categoria;
import com.lucasaquila.cursomc.domain.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

	/**
	 * @param nome
	 * @param categorias
	 * @param pageRequest
	 * @return
	 */
	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT produto FROM Produto produto "
			+ "INNER JOIN produto.categorias categoria "
			+ "WHERE produto.nome LIKE %:nome% AND "
			+ "categoria IN :categorias")
	Page<Produto> search(@Param("nome") String nome, @Param("categorias") List<Categoria> categorias, Pageable pageRequest);
	
//	A busca poderia ser implementada apenas pela chamada do nome
//	Page<Produto> findDistinctByNomeContainingAndCategoriasIn(String nome,List<Categoria> categorias, Pageable pageRequest);

}
