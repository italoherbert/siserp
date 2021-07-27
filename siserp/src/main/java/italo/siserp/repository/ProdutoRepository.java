package italo.siserp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import italo.siserp.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

}
