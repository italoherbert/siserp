package italo.siserp.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "compra")
public class Compra {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column
	@Temporal(TemporalType.DATE)
	private Date dataCompra;
		
	@OneToMany(mappedBy="compra", cascade=CascadeType.ALL)
	private List<ItemCompra> itensCompra;
	
	@ManyToOne(cascade= {CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name="fornecedor_id")
	private Fornecedor fornecedor;
	
	@OneToMany(mappedBy="compra", cascade=CascadeType.ALL)
	private List<CompraParcela> parcelas;
	
}
