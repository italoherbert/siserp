package italo.siserp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import italo.siserp.model.UsuarioGrupo;

public interface UsuarioGrupoRepository extends JpaRepository<UsuarioGrupo, Long>{

	@Query( "select g from UsuarioGrupo g where lower(g.nome) = lower(?1)" )
	public Optional<UsuarioGrupo> buscaPorNome( String nome );
	
	@Query( "select g from UsuarioGrupo g where lower(g.nome) like lower(?1) order by (g.nome)" )
	public List<UsuarioGrupo> filtra( String nome );		
	
	@Query( "select g from UsuarioGrupo g order by (g.nome)")
	public List<UsuarioGrupo> buscaTodos();
	
}
