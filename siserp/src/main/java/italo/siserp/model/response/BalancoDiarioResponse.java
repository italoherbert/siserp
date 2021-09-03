package italo.siserp.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BalancoDiarioResponse {

	private String dataAbertura;
	
	private String debito;
	
	private String credito;
	
	private String saldo;
	
	private String cartaoValorRecebido;
	
	private String totalVendasAPrazo;
	
}
