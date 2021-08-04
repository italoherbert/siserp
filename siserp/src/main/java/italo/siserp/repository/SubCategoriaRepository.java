package italo.siserp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import italo.siserp.model.SubCategoria;

public interface SubCategoriaRepository extends JpaRepository<SubCategoria, Long>{

	@Query( "select sc from SubCategoria sc join sc.categoria c "
			+ "where lower(c.descricao) = lower(?1) and lower(sc.descricao) like lower(?2)" )
	public List<SubCategoria> filtra( String categoria, String descricaoIni, Pageable p );
	
	@Query( "select sc from SubCategoria sc join sc.categoria c "
			+ "where lower(c.descricao) = lower(?1) and lower(sc.descricao) = lower(?2)")
	public Optional<SubCategoria> buscaPorDescricao( String categoria, String descricao );
	
}
