package italo.siserp.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.model.CompraParcela;
import italo.siserp.model.Fornecedor;
import italo.siserp.model.response.CompraParcelaResponse;
import italo.siserp.model.response.ContaPagarResponse;

@Component
public class ContaPagarBuilder {
	
	@Autowired
	private CompraParcelaBuilder compraParcelaBuilder;
		
	public void carregaContaPagarResponse( ContaPagarResponse resp, CompraParcela parcela ) {
		CompraParcelaResponse parcelaResp = compraParcelaBuilder.novoCompraParcelaResponse();
		compraParcelaBuilder.carregaCompraParcelaResponse( parcelaResp, parcela );
		
		Fornecedor f = parcela.getCompra().getFornecedor();
				
		resp.setParcela( parcelaResp );
		resp.setFornecedorEmpresa( f.getEmpresa() );
	}	
	
	public ContaPagarResponse novoContaPagarResponse() {
		return new ContaPagarResponse();
	}
	
}

