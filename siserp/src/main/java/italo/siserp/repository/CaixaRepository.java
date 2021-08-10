package italo.siserp.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import italo.siserp.model.Caixa;

public interface CaixaRepository extends JpaRepository<Caixa, Long> {
	
	@Query( "select c from Caixa c join c.funcionario f where f.id=?1 and c.dataAbertura=?2")
	public Optional<Caixa> buscaCaixa( Long funcionarioId, Date dataAbertura );
	
	@Query( "select c from Caixa c join c.funcionario f join f.pessoa p "
			+ "where c.dataAbertura between ?1 and ?2 and lower( p.nome ) like lower(?3)")
	public List<Caixa> filtra( Date dataIni, Date dataFim, String funcionario );
	
	@Query( "select c from Caixa c where c.dataAbertura between ?1 and ?2")
	public List<Caixa> filtraSemFuncionario( Date dataIni, Date dataFim );
		
}
