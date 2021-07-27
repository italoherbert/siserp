package italo.siserp.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FuncionarioResponse {

	private Long id;
	
	private PessoaResponse pessoa;
	
	private UsuarioResponse usuario;
		
}
