package italo.siserp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import italo.siserp.model.SubCategoria;

public interface SubCategoriaRepository extends JpaRepository<SubCategoria, Long>{

	@Query( "select sc from SubCategoria sc join sc.categoria c "
			+ "where c.id=?1 and lower(sc.descricao) like lower(?2)" )
	public List<SubCategoria> filtra( Long categoriaId, String descricaoIni );
	
	@Query( "select count(*)=1 from SubCategoria sc join sc.categoria c "
			+ "where c.id=?1 and lower(sc.descricao)=lower(?2)" )
	public boolean existePorDescricao( Long categoriaId, String descricao );
	
}
