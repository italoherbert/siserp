package italo.siserp.model.response;

import lombok.Setter;

import lombok.Getter;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Setter
public class ClienteResponse {

	private Long id;
	
	private PessoaResponse pessoa;
	
}
