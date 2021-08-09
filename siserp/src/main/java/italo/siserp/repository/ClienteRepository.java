package italo.siserp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import italo.siserp.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

	@Query( "select c from Cliente c join c.pessoa p where lower(p.nome) like lower(?1)")
	public List<Cliente> filtra( String nomeIni, Pageable p );
	
	@Query( "select c from Cliente c join c.pessoa p where lower(p.nome) = lower(?1)")
	public Optional<Cliente> buscaPorNome( String nome );
	
}
