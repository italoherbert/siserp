package italo.siserp.model.request;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SaveCategoriaRequest {
	
	private String descricao;
	
	private List<SaveSubCategoriaRequest> subcategorias;
	
}
