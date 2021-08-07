package italo.siserp.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CaixaResponse {

	private Long id;
	
	private String dataAbertura;
	
	private String valorInicial;
	
	private String valor;
	
	private FuncionarioResponse funcionario;
		
}
