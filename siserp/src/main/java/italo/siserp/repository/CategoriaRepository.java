package italo.siserp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import italo.siserp.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

	@Query( "select c from Categoria c where lower(c.descricao) like lower(:descricaoIni)" )
	public List<Categoria> filtra( @Param("descricaoIni") String descricaoIni );
	
	public Optional<Categoria> findByDescricao( String descricao );

}
