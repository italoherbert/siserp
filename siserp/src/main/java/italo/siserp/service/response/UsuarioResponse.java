package italo.siserp.service.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UsuarioResponse {

	private Long id;	
	
	private String username;
			
	private UsuarioGrupoResponse grupo;
		
}
