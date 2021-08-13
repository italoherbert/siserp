package italo.siserp.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.model.CompraParcela;
import italo.siserp.model.response.ContaPagarResponse;
import italo.siserp.model.response.ContasPagarResponse;
import italo.siserp.util.NumeroUtil;

@Component
public class ContasPagarBuilder {

	@Autowired
	private ContaPagarBuilder contaPagarBuilder;
	
	@Autowired
	private NumeroUtil numeroUtil;
	
	public void carregaContasPagarResponse( 
			ContasPagarResponse response, 
			List<CompraParcela> parcelas, 
			double debitoTotalCompleto, boolean incluirPagas ) {
		
		double debitoTotalPeriodo = 0;
		
		List<ContaPagarResponse> lista = new ArrayList<>();
		for( CompraParcela p : parcelas ) {
			if ( !incluirPagas && p.isPaga() )
				continue;
			
			ContaPagarResponse resp = contaPagarBuilder.novoContaPagarResponse();
			contaPagarBuilder.carregaContaPagarResponse( resp, p );
			
			lista.add( resp );							
			
			if ( !p.isPaga() )
				debitoTotalPeriodo += p.getValor();
		}
		
		response.setContas( lista );
		response.setDebitoTotalPeriodo( numeroUtil.doubleParaString( debitoTotalPeriodo ) ); 
		response.setDebitoTotalCompleto( numeroUtil.doubleParaString( debitoTotalCompleto ) );		
	}
	
	public ContasPagarResponse novoContasPagarResponse() {
		return new ContasPagarResponse();
	}
	
}
