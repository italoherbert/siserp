package italo.siserp.service.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BuscaContasPagarRequest {

	private String dataIni;
	
	private String dataFim;
	
	private String incluirFornecedor;
	
	private String fornecedorNomeIni;
	
	private String incluirPagas;
	
}
