package italo.siserp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import italo.siserp.model.Fornecedor;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

	@Query( "select f from Fornecedor f where lower(f.empresa) like lower(?1)" )
	public List<Fornecedor> filtra( String empresaIni, Pageable p );
		
	@Query( "select f from Fornecedor f where lower(f.empresa) = lower(?1)")
	public Optional<Fornecedor> buscaPorEmpresa( String empresa );
	
}
