package italo.siserp.model.request;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SaveVendaRequest {

	private String clienteId;
	
	private String incluirCliente;
		
	private String subtotal;
	
	private String desconto;
		
	private String valorPago;
	
	private String formaPag;
	
	private List<SaveItemVendaRequest> itensVenda;
	
}
