package italo.siserp.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.model.VendaParcela;
import italo.siserp.service.response.ContaReceberResponse;
import italo.siserp.service.response.ContasReceberResponse;
import italo.siserp.util.NumeroUtil;

@Component
public class ContasReceberBuilder {

	@Autowired
	private ContaReceberBuilder contaReceberBuilder;
	
	@Autowired
	private NumeroUtil numeroUtil;
	
	public void carregaContasReceberResponse( 
			ContasReceberResponse response, 
			List<VendaParcela> parcelas, double totalCompleto, boolean incluirPagas ) {
		
		double totalPeriodo = 0;
		
		List<ContaReceberResponse> lista = new ArrayList<>();
		for( VendaParcela p : parcelas ) {						
			if ( !incluirPagas && p.getDebito()==0 )
				continue;
			
			ContaReceberResponse resp = contaReceberBuilder.novoContaReceberResponse();
			contaReceberBuilder.carregaContaReceberResponse( resp, p );
						
			lista.add( resp );
			
			if ( p.getDebito()>0 )
				totalPeriodo += p.getValor();
		}
		
		response.setContas( lista );
		response.setTotalPeriodo( numeroUtil.doubleParaString( totalPeriodo ) ); 
		response.setTotalCompleto( numeroUtil.doubleParaString( totalCompleto ) );		
	}
	
	public ContasReceberResponse novoContasReceberResponse() {
		return new ContasReceberResponse();
	}
	
}
