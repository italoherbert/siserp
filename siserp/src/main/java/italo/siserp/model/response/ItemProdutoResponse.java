package italo.siserp.model.response;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ItemProdutoResponse {

	private Long id;
	
	private String quantidade;
	
	private List<CategoriaResponse> categorias;
	
}
