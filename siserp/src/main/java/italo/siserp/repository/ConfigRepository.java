package italo.siserp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import italo.siserp.model.Config;

public interface ConfigRepository extends JpaRepository<Config, Long> {
	
}
