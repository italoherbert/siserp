package italo.siserp.service.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BuscaContasReceberRequest {

	private String incluirPagas;
	
	private String incluirCliente;
	
	private String clienteNomeIni;
	
	private String dataIni;
	
	private String dataFim;
	
}
