package italo.siserp.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "categoria_map")
public class CategoriaMap {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(cascade= {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name="categoria_id") 
	private Categoria categoria;
	
	@ManyToOne(cascade= {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name="subcategoria_id") 
	private SubCategoria subcategoria;
	
	@ManyToOne
	@JoinColumn(name="produto_id") 
	private Produto produto;
}
