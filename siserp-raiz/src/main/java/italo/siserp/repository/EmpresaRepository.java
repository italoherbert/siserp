package italo.siserp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import italo.siserp.model.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
	
}
