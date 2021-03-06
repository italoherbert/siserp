package italo.siserp.model;

import java.util.Date;

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
@Table(name = "compra_parcela")
public class CompraParcela {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private double valor;
		
	@Column
	@Temporal(TemporalType.DATE)	
	private Date dataPagamento;
	
	@Column
	@Temporal(TemporalType.DATE)
	private Date dataVencimento;
	
	@Column	
	private boolean paga;
	
	@ManyToOne
	@JoinColumn(name="compra_id")
	private Compra compra;

}
