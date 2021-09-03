package italo.siserp.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SavePessoaRequest {
	
	private String nome;
	
	private String telefone;
	
	private String email;
	
	private SaveEnderecoRequest endereco;
	
}
