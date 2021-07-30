package italo.siserp.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TotalCompraResponse {

	private Long id;
	
	private String dataCompra;
	
	private String debitoTotal; 
	
}
