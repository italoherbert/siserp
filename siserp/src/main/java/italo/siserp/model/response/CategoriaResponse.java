package italo.siserp.model.response;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoriaResponse {

	private Long id;
	
	private String descricao;
	
	private List<SubCategoriaResponse> subcategorias;
	
}
