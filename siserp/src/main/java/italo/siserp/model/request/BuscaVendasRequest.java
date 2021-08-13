package italo.siserp.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BuscaVendasRequest {

	private String dataIni;
	
	private String dataFim;
	
	private String clienteNomeIni;
	
	private String incluirCliente;
		
	private String incluirPagas;
	
}
