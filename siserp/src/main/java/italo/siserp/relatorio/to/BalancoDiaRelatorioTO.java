package italo.siserp.relatorio.to;

import java.util.List;

import italo.siserp.service.response.CaixaBalancoResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BalancoDiaRelatorioTO {

	private List<CaixaBalancoResponse> balancosPorCaixa;
	
	private String creditoTotal;
	
	private String debitoTotal;
	
	private String saldoTotal;
	
}
