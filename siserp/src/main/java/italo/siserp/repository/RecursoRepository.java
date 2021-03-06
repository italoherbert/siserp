package italo.siserp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import italo.siserp.model.Recurso;

public interface RecursoRepository extends JpaRepository<Recurso, Long> {

	@Query( "select r from Recurso r where lower(r.nome)=lower(?1)")
	public Optional<Recurso> buscaPorNome( String nome );
	
	@Query( "select r from Recurso r where lower(r.nome) like lower(?1) order by (r.nome)")
	public List<Recurso> filtra( String nomeIni );
	
}
