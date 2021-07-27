package italo.siserp.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "categoria")
public class Categoria {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String descricao;
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="categoria", cascade=CascadeType.ALL)
	private List<SubCategoria> subcategorias;
	
	@OneToMany(mappedBy = "categoria", cascade=CascadeType.ALL)	
	private List<ItemProduto> itensProdutos;
	
}