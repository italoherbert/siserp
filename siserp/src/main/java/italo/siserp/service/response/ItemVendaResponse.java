package italo.siserp.service.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ItemVendaResponse {
	
	private Long id;
	
	private String quantidade;
	
	private String precoUnitario;
	
	private ProdutoResponse produto;
	
}
