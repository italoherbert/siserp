package italo.siserp.service.request;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SaveVendaRequest {

	private String clienteNome;
	
	private String incluirCliente;
		
	private String subtotal;
	
	private String desconto;
			
	private String formaPag;
	
	private List<SaveItemVendaRequest> itensVenda;
	
}
