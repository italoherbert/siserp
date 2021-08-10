package italo.siserp.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LancamentoResponse {

	private Long id;
	
	private String tipo;
	
	private String valor;
	
	private String dataOperacao;
	
	private String obs;
	
}
