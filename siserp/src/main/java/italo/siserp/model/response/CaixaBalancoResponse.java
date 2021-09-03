package italo.siserp.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CaixaBalancoResponse {

	private String funcionarioNome;
	
	private String dataAbertura;
	
	private String debito;
	
	private String credito;
	
	private String cartaoValorRecebido;
	
	private String totalVendasAPrazo;
	
	private String saldo;
	
}
