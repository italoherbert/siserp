package italo.siserp.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name="venda_parcela")
public class VendaParcela {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private double valor;
	
	@Column
	private double debito;
	
	@Column
	private double debitoAux;
	
	@Column
	private boolean debitoRestaurado;
		
	@Column
	@Temporal(TemporalType.DATE)	
	private Date dataPagamento;
	
	@Column
	@Temporal(TemporalType.DATE)
	private Date dataVencimento;
		
	@ManyToOne(cascade = {CascadeType.PERSIST})
	@JoinColumn(name="venda_id")
	private Venda venda;
	
}
