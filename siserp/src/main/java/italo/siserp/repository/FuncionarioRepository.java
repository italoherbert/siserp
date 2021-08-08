package italo.siserp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import italo.siserp.model.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

	@Query( "select f from Funcionario f join f.pessoa p join f.usuario u " +
			"where lower(p.nome) like lower(?1) or lower(u.username) like lower(?2)" )
	public List<Funcionario> filtra( String nomeIni, String usernameIni ); 
	
}
