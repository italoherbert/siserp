package italo.siserp.service.response;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CaixaResponse {

	private Long id;
	
	private String dataAbertura;
			
	private FuncionarioResponse funcionario;
	
	private List<LancamentoResponse> lancamentos;
		
}
