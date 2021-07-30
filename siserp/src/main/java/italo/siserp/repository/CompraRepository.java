package italo.siserp.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import italo.siserp.model.Compra;

public interface CompraRepository extends JpaRepository<Compra, Long> {

	@Query( "select c from Compra c where c.dataCompra between ?1 and ?2")
	public List<Compra> filtra( Date dataIni, Date dataFim );
	
}
