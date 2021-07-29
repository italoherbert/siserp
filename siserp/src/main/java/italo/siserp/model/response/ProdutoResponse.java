package italo.siserp.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ProdutoResponse {

	private Long id;
	
	private String descricao;
	
	private String precoUnitCompra;
	
	private String precoUnitVenda;
	
	private String unidade;
	
	private String codigoBarras;
	
}
