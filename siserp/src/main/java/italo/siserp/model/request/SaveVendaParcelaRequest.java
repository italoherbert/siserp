package italo.siserp.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SaveVendaParcelaRequest {

	private String valor;
	
	private String dataPagamento;
	
	private String dataVencimento;
	
}
