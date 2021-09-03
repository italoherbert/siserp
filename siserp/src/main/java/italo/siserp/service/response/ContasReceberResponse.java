package italo.siserp.service.response;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ContasReceberResponse {
		
	private String totalCompleto;
	
	private String totalPeriodo;
	
	private List<ContaReceberResponse> contas;
	
}
