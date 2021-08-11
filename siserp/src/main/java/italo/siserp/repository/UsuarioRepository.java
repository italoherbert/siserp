package italo.siserp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import italo.siserp.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	@Query( "select u from Usuario u where lower( u.username ) like lower(?1)" )
	public List<Usuario> buscaPorUsernameIni( String usernameIni );
	
	public Optional<Usuario> findByUsername( String username );
	
}
