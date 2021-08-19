package italo.siserp.service.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SaveCompraParcelaRequest {

	private String valor;
	
	private String dataPagamento;
	
	private String dataVencimento;
	
}
