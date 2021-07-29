package italo.siserp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import italo.siserp.model.CategoriaMap;

public interface CategoriaMapRepository extends JpaRepository<CategoriaMap, Long>{

	@Query( "select ip from CategoriaMap map "
				+ "join map.itemProduto ip "
				+ "join map.categoria c "
				+ "join map.subcategoria sc "
				+ "join ip.produto p "
			+ "where p.codigoBarras=?1 and c.descricao=?2 and sc.descricao=?3" ) 
	public Optional<CategoriaMap> temCategoria( String codigoBarras, String categoria, String subcategoria );
	
}
