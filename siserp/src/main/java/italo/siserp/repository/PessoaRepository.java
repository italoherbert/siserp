package italo.siserp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import italo.siserp.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
	
}
