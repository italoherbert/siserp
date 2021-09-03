package italo.siserp.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import italo.siserp.model.Venda;

public interface VendaRepository extends JpaRepository<Venda, Long> {
		
	@Query( "select v from Venda v "
			+ "where v.dataVenda between ?1 and ?2 "
			+ "order by ( v.dataVenda )")
	public List<Venda> filtra( Date dataIni, Date dataFim );
	
	@Query( "select v from Venda v join v.cliente c where c.id=?1" )
	public List<Venda> buscaVendasPorClienteId( Long clienteId );
	
}
