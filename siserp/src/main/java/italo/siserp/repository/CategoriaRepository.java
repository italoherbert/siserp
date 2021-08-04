package italo.siserp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import italo.siserp.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

	@Query( "select c from Categoria c where lower(c.descricao) like lower(?1)" )
	public List<Categoria> filtra( String descricaoIni, Pageable p );
	
	@Query( "select c from Categoria c where lower(c.descricao) = lower(?1)")
	public Optional<Categoria> buscaPorDescricao( String descricao );

}
