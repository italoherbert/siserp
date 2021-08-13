package italo.siserp.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import italo.siserp.model.CompraParcela;

public interface CompraParcelaRepository extends JpaRepository<CompraParcela, Long> {

	@Query( "select p from CompraParcela p join p.compra c join c.fornecedor f "
			+ "where p.dataPagamento between ?1 and ?2 and "
				+ "lower(f.empresa) like lower(?3) and "
				+ "(?4=true or p.paga=false)"
			+ "order by ( p.dataPagamento )")
	public List<CompraParcela> filtra( 
			Date dataIni, Date dataFim, String fornecedorEmpresaIni, boolean incluirPagas );
	
	@Query( "select p from CompraParcela p "
			+ "where p.dataPagamento between ?1 and ?2 and "
				+ "(?3=true or p.paga=false) "
			+ "order by ( p.dataPagamento )")
	public List<CompraParcela> filtraSemFornecedor( 
			Date dataIni, Date dataFim, boolean incluirPagas );
	
	@Query( "select sum(p.valor) from CompraParcela p where p.paga=false")
	public Double calculaDebitoTotalCompleto();
	
}
