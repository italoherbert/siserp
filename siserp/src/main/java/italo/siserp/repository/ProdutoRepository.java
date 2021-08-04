package italo.siserp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import italo.siserp.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
	
	@Query( "select p from Produto p where lower(p.descricao) like lower(?1)" )
	public List<Produto> filtraPorDescIni( String descricaoIni );

	public Optional<Produto> findByCodigoBarras( String codigoBarras );
	
	public boolean existsByCodigoBarras( String codigoBarras );
	
}
