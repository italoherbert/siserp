package italo.siserp.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SaveProdutoRequest {
	
	private String descricao;
	
	private String precoUnitCompra;
	
	private String precoUnitVenda;
	
	private String unidade;
	
	private String codigoBarras;
			
}
