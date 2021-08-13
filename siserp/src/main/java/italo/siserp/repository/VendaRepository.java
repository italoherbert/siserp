package italo.siserp.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import italo.siserp.model.Venda;

public interface VendaRepository extends JpaRepository<Venda, Long> {
		
	@Query( "select v from Venda v join v.cliente c join c.pessoa p "
			+ "where v.dataVenda between ?1 and ?2 and "
				+ "lower(p.nome) like lower(?3) and "
				+ "(?4=true or v.debito>0) "
			+ "order by ( v.dataVenda)")
	public List<Venda> filtra( Date dataIni, Date dataFim, String clienteIni, boolean incluirPagas );
	
	@Query( "select v from Venda v "
			+ "where v.dataVenda between ?1 and ?2 and "
				+ "(?3=true or v.debito>0) "
			+ "order by( v.dataVenda )")
	public List<Venda> filtraSemCliente( Date dataIni, Date dataFim, boolean incluirPagas );
	
	@Query( "select v from Venda v join v.cliente c where c.id=?1 and v.debito>0" )
	public List<Venda> buscaVendasPorClienteId( Long clienteId );
	
	@Query( "select sum(v.debito) from Venda v where v.debito>0 and v.cliente is not null" )
	public Double calculaContasReceberTotalCompleto();
		
}
