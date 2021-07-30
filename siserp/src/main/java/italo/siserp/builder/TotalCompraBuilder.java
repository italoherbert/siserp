package italo.siserp.builder;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.model.Compra;
import italo.siserp.model.ItemCompra;
import italo.siserp.model.response.TotalCompraResponse;
import italo.siserp.util.DataUtil;
import italo.siserp.util.NumeroUtil;

@Component
public class TotalCompraBuilder {
	
	@Autowired
	private DataUtil dataUtil;
	
	@Autowired
	private NumeroUtil numeroUtil;
	
	public void carregaTotalCompraResponse( TotalCompraResponse resp, Compra c ) {
		resp.setId( c.getId() );
		resp.setDataCompra( dataUtil.dataParaString( c.getDataCompra() ) ); 
		
		double total = 0;
		List<ItemCompra> itens = c.getItensCompra();
		for( ItemCompra ic : itens )
			total += ic.getPrecoUnitario() * ic.getQuantidade();

		resp.setDebitoTotal( numeroUtil.doubleParaString( total ) ); 
	}	
	
	public TotalCompraResponse novoTotalCompraResponse() {		
		return new TotalCompraResponse();
	}
	
}
