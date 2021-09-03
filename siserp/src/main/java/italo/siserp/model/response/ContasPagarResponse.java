package italo.siserp.model.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ContasPagarResponse {

	private String debitoTotalCompleto;
	
	private String debitoTotalPeriodo;
	
	private List<ContaPagarResponse> contas;
	
}
