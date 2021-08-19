package italo.siserp.service.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PermissaoGrupoResponse {

	private Long id;
		
	private String recurso;
	
	private String leitura;
	
	private String escrita;
	
	private String remocao;
	
}
