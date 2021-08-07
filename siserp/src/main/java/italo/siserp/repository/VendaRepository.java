package italo.siserp.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import italo.siserp.model.Venda;

public interface VendaRepository extends JpaRepository<Venda, Long> {
		
	@Query( "select v from Venda v join v.cliente c join c.pessoa p "
			+ "where v.dataVenda between ?1 and ?2 and lower(p.nome) like lower(?3)")
	public List<Venda> filtra( Date dataIni, Date dataFim, String clienteIni );
	
	@Query( "select v from Venda v where v.dataVenda between ?1 and ?2")
	public List<Venda> filtraSemCliente( Date dataIni, Date dataFim );
	
}
