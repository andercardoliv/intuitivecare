package br.com.intuitivecare.desafio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.intuitivecare.desafio.model.CategoriaPadrao;

public interface CategoriaPadraoRepository extends JpaRepository<CategoriaPadrao, Long> {
	
	@Query("SELECT c.codigo FROM CategoriaPadrao c WHERE c.codigo = :codigo")
	Integer findByCodigo(@Param("codigo") Integer codigo);
}
