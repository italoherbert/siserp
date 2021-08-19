package italo.siserp.service.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class EfetuarPagamentoRequest {

	private Long clienteId;
	
	private String valorPago;
	
	private String formaPag;
	
}
