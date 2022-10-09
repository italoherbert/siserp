package italo.siserp.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import italo.siserp.model.Venda;

public interface VendaRepository extends JpaRepository<Venda, Long> {
		
	@Query( "select v from Venda v "
			+ "where v.dataVenda between ?1 and ?2 "
			+ "order by v.dataVenda")
	public List<Venda> filtra( Date dataIni, Date dataFim );
	
	@Query( "select v from Venda v join v.cliente c where c.id=?1 order by v.dataVenda" )
	public List<Venda> buscaVendasPorClienteId( Long clienteId );
	
	@Query( "select v from Venda v join v.cliente c "
			+ "where c.id=?1 and "
			+ 		"(select sum(debito) from VendaParcela vp where vp.venda.id=v.id)>0 "
			+ "order by v.dataVenda" )
	public List<Venda> buscaVendasEmDebitoPorClienteId( Long clienteId );
	
}
