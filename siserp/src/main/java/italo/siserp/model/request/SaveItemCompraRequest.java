package italo.siserp.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SaveItemCompraRequest {
	
	private String quantidade;
	
	private String precoUnitario;
	
	private SaveProdutoRequest produto;
		
}
