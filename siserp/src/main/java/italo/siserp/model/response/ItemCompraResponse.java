package italo.siserp.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ItemCompraResponse {

	private Long id;
	
	private String quantidade;
		
	private String precoUnitario;
	
	private ProdutoResponse produto;
	
}
