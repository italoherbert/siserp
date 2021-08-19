package italo.siserp.service.request;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SaveCompraRequest {
		
	private String dataCompra;

	private SaveFornecedorRequest fornecedor;
	
	private List<SaveItemCompraRequest> itensCompra;	

	private List<SaveCompraParcelaRequest> parcelas;

}
