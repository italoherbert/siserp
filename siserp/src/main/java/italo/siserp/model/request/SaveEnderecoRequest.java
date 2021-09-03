package italo.siserp.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SaveEnderecoRequest {

	private String ender;
	
	private String logradouro;
	
	private String numero;
	
	private String bairro;
	
	private String cidade;
	
	private String uf;
	
}
