package italo.siserp.model.response;

import lombok.Setter;

import lombok.Getter;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Setter
public class CaixaBalancoResponse {

	private String funcionarioNome;
	
	private String dataAbertura;
	
	private String debito;
	
	private String credito;
	
	private String cartaoValorRecebido;
	
	private String valorTotalVendasAPrazo;
	
	private String saldo;
	
}
