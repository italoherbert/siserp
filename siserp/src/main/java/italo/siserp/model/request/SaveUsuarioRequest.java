package italo.siserp.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SaveUsuarioRequest {
	
	private String username;
	
	private String password;
	
	private SaveUsuarioGrupoRequest grupo;
		
}
