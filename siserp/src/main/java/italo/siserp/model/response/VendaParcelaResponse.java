package italo.siserp.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class VendaParcelaResponse {

	private Long id;
	
	private String valor;
	
	private String debito;
	
	private String dataPagamento;
	
	private String dataVencimento;
	
	private String debitoRestaurado;
		
}
