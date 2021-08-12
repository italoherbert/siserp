package italo.siserp.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.model.Venda;
import italo.siserp.model.response.ContasReceberResponse;
import italo.siserp.model.response.VendaResponse;
import italo.siserp.util.NumeroUtil;

@Component
public class ContasReceberBuilder {

	@Autowired
	private VendaBuilder vendaBuilder;
	
	@Autowired
	private NumeroUtil numeroUtil;
	
	public void carregaContasReceberResponse( 
			ContasReceberResponse response, 
			List<Venda> vendas, double totalCompleto ) {
		
		double totalPeriodo = 0;
		
		List<VendaResponse> lista = new ArrayList<>();
		for( Venda v : vendas ) {
			if ( v.getCliente() == null )
				continue;
			
			VendaResponse resp = vendaBuilder.novoVendaResponse();
			vendaBuilder.carregaVendaResponse( resp, v );
			
			lista.add( resp );
			
			totalPeriodo += v.getDebito();
		}
		
		response.setVendas( lista );
		response.setTotalPeriodo( numeroUtil.doubleParaString( totalPeriodo ) ); 
		response.setTotalCompleto( numeroUtil.doubleParaString( totalCompleto ) );		
	}
	
	public ContasReceberResponse novoContasReceberResponse() {
		return new ContasReceberResponse();
	}
	
}
