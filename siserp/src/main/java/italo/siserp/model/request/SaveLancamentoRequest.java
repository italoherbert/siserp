package italo.siserp.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SaveLancamentoRequest {

	private String tipo;
	
	private String valor;
	
	private String obs;
	
}
