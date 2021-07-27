package italo.siserp.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SaveFuncionarioRequest {

	private SavePessoaRequest pessoa;
	
	private SaveUsuarioRequest usuario;	
		
}
