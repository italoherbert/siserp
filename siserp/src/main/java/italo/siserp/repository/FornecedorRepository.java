package italo.siserp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import italo.siserp.model.Fornecedor;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

	@Query( "select f from Fornecedor f where lower(f.empresa) like lower(:empresaIni)" )
	public List<Fornecedor> filtra( @Param("empresaIni") String empresaIni );
		
	public Optional<Fornecedor> findByEmpresa( String empresa );
	
}
