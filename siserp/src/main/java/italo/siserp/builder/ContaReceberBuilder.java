package italo.siserp.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.model.Cliente;
import italo.siserp.model.VendaParcela;
import italo.siserp.model.response.ContaReceberResponse;
import italo.siserp.model.response.VendaParcelaResponse;

@Component
public class ContaReceberBuilder {

	@Autowired
	private VendaParcelaBuilder vendaParcelaBuilder;
		
	public void carregaContaReceberResponse( ContaReceberResponse resp, VendaParcela parcela ) {
		VendaParcelaResponse parcelaResp = vendaParcelaBuilder.novoVendaParcelaResponse();
		vendaParcelaBuilder.carregaVendaParcelaResponse( parcelaResp, parcela );
		
		Cliente c = parcela.getVenda().getCliente();
				
		resp.setParcela( parcelaResp );
		resp.setClienteNome( c.getPessoa().getNome() ); 
	}	
	
	public ContaReceberResponse novoContaReceberResponse() {
		return new ContaReceberResponse();
	}
	
}
