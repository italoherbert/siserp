package italo.siserp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import italo.siserp.model.CategoriaMap;

public interface CategoriaMapRepository extends JpaRepository<CategoriaMap, Long>{
	
	@Query( "select map from CategoriaMap map "
			+ "where lower(map.categoria)=lower(?1) and lower(map.subcategoria)=lower(?2)" )
	public Optional<CategoriaMap> get( String categoria, String subcategoria );
	
	@Query( "select map from CategoriaMap map "
			+ "where lower(map.categoria) like lower(?1) or lower(map.subcategoria) like lower(?2)" )
	public List<CategoriaMap> filtra( String categoria, String subcategoria, Pageable p );
	
	@Query( "select map from CategoriaMap map where lower(map.categoria) like lower(?1)")
	public List<CategoriaMap> filtraCategorias( String categoria, Pageable p );
	
	@Query( "select map from CategoriaMap map where lower(map.categoria)=lower(?1) and lower(map.subcategoria) like lower(?2)")
	public List<CategoriaMap> filtraSubcategorias( String categoria, String subcategoriaIni, Pageable p );
	
}
