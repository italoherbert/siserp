package italo.siserp.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import italo.siserp.model.CompraParcela;

public interface CompraParcelaRepository extends JpaRepository<CompraParcela, Long> {

	@Query( "select p from CompraParcela p where p.dataPagamento between ?1 and ?2 order by ( p.dataPagamento )")
	public List<CompraParcela> filtra( Date dataIni, Date dataFim );
	
}
