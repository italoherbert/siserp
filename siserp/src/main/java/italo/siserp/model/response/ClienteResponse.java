package italo.siserp.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ClienteResponse {

	private Long id;
	
	private PessoaResponse pessoa;
	
}
