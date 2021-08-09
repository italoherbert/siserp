package italo.siserp.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "venda")
public class Venda {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private double subtotal;

	@Column
	private double desconto;

	@Column
	private double debito;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataVenda;
	
	@Column
	@Enumerated(EnumType.STRING)
	private FormaPag formaPag;
	
	@ManyToOne(optional = true)
	@JoinColumn( name="cliente_id" )
	private Cliente cliente;
	
	@ManyToOne
	@JoinColumn(name="caixa_id") 
	private Caixa caixa;
	
	@OneToMany(mappedBy="venda", cascade=CascadeType.REMOVE)
	private List<ItemVenda> itensVenda;
	
}
