package italo.siserp.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CompraParcelaResponse {

	private Long id;
	
	private String valor;
	
	private String dataPagamento;
	
	private String dataVencimento;
	
}
