package italo.siserp.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "produto")
public class Produto {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String descricao;

	@Column
	private double precoUnitarioCompra;

	@Column
	private double precoUnitarioVenda;

	@Column
	private double quantidade;

	@Column
	private String unidade;
		
	@Column(unique = true)
	private String codigoBarras;
		
	@OneToMany(mappedBy="produto", cascade=CascadeType.REMOVE)
	private List<ItemCompra> itensCompra;
	
	@OneToMany(mappedBy="produto", cascade = CascadeType.ALL)
	private List<ProdutoImpostoMap> produtoImpostoMap;
	
	@OneToMany(mappedBy="produto", cascade = CascadeType.ALL)
	private List<CategoriaMap> categoriaMaps;
	
}
