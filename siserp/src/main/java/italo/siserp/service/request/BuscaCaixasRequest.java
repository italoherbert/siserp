package italo.siserp.service.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BuscaCaixasRequest {

	private String dataIni;
	
	private String dataFim;
	
	private String incluirFuncionario;
	
	private String funcionarioNomeIni;
	
}
