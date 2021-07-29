package italo.siserp.model.request;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SaveItemProdutoRequest {

	private String quantidade;
	
	private List<SaveCategoriaRequest> categorias;
	
}
