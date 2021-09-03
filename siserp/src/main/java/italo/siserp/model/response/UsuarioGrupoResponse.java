package italo.siserp.model.response;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UsuarioGrupoResponse {

	private Long id;
	
	private String nome;
	
	private List<PermissaoGrupoResponse> permissaoGrupos;
	
}

