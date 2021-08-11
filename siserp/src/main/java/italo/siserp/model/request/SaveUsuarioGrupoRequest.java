package italo.siserp.model.request;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SaveUsuarioGrupoRequest {

	private String nome;
	
	private List<SavePermissaoGrupoRequest> permissaoGrupos;
	
}
