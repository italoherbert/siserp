package italo.siserp.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import italo.siserp.model.VendaParcela;

public interface VendaParcelaRepository extends JpaRepository<VendaParcela, Long> {
	
	@Query( "select p from VendaParcela p join p.venda c join c.cliente f join f.pessoa pes "
			+ "where p.dataPagamento between ?1 and ?2 and "
				+ "lower(pes.nome) like lower(?3) and "
				+ "(?4=true or p.debito>0) "
			+ "order by ( p.dataPagamento )" )
	public List<VendaParcela> filtra( 
			Date dataIni, Date dataFim, String clienteNomeIni, boolean incluirPagas );
	
	@Query( "select p from VendaParcela p "
			+ "where p.dataPagamento between ?1 and ?2 and "
				+ "(?3=true or p.debito>0) "
			+ "order by ( p.dataPagamento )" )
	public List<VendaParcela> filtraSemCliente( 
			Date dataIni, Date dataFim, boolean incluirPagas );
	
	@Query( "select p from VendaParcela p where p.venda.id=?1 order by p.dataPagamento") 
	public List<VendaParcela> buscaParcelas( Long vendaId );
	
	@Query( "select sum(p.valor) from VendaParcela p where p.debito>0" )
	public Double calculaDebitoTotalCompleto();
	
}
