package italo.siserp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import italo.siserp.model.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

	@Query( "select f from Funcionario f join f.pessoa p join f.usuario u " +
			"where lower(p.nome) like lower(:nomeIni) and lower(u.username) like lower(:usernameIni)" )
	public List<Funcionario> filtra( @Param("nomeIni") String nomeIni, @Param("usernameIni") String usernameIni );
	
}
