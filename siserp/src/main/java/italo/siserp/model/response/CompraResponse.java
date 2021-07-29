package italo.siserp.model.response;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CompraResponse {

	private Long id;
	
	private String dataCompra;
	
	private FornecedorResponse fornecedor;
	
	private List<CompraParcelaResponse> parcelas;	
	
	private List<ItemCompraResponse> itens;		
	
}
