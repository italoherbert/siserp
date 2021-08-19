package italo.siserp.dao.bean;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CaixaBalancoDAOTO {

	private String funcionarioNome;
	
	private Date dataAbertura;
	
	private double debito;
	
	private double credito;
	
	private double saldo;
	
	private double cartaoValorRecebido;
	
	private double vendasAPrazoTotal;
	
}
