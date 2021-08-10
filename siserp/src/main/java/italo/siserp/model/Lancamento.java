package italo.siserp.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "lancamento")
public class Lancamento {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private double valor;
		
	@Column
	@Enumerated(EnumType.STRING)
	private LancamentoTipo tipo;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataOperacao;
	
	@ManyToOne
	@JoinColumn(name="caixa_id")
	private Caixa caixa;
	
}
