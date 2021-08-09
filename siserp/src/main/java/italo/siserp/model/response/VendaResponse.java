package italo.siserp.model.response;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class VendaResponse {

	private Long id;
	
	private String dataVenda;
	
	private String subtotal;
	
	private String desconto;
	
	private String debito;
	
	private String formaPag;

	private ClienteResponse cliente;

	private List<ItemVendaResponse> itens;
	
}
