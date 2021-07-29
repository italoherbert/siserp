package italo.siserp.builder;

import org.springframework.stereotype.Component;

import italo.siserp.exception.PrecoUnitCompraInvalidoException;
import italo.siserp.exception.PrecoUnitVendaInvalidoException;
import italo.siserp.exception.QuantidadeInvalidaException;
import italo.siserp.model.Compra;
import italo.siserp.model.ItemCompra;
import italo.siserp.model.Produto;
import italo.siserp.model.request.SaveItemCompraRequest;
import italo.siserp.model.response.ItemCompraResponse;

@Component
public class ItemCompraBuilder {
		
	public void carregaItemCompra( ItemCompra item, SaveItemCompraRequest req ) 
			throws PrecoUnitCompraInvalidoException,
				PrecoUnitVendaInvalidoException,
				QuantidadeInvalidaException {
		try {
			item.setPrecoUnitario( Double.parseDouble( req.getPrecoUnitario() ) );
		} catch ( NumberFormatException e ) {
			PrecoUnitCompraInvalidoException ex = new PrecoUnitCompraInvalidoException();
			ex.setParams( req.getPrecoUnitario() );
			throw ex;
		}
		
		try {
			item.setQuantidade( Double.parseDouble( req.getQuantidade() ) ); 
		} catch ( NumberFormatException e ) {
			QuantidadeInvalidaException ex = new QuantidadeInvalidaException();
			ex.setParams( req.getPrecoUnitario() );
			throw ex;
		}					
	}
	
	public void carregaItemCompraResponse( ItemCompraResponse resp, ItemCompra item ) {
		resp.setId( item.getId() ); 
		resp.setPrecoUnitario( String.valueOf( item.getPrecoUnitario() ) );
		resp.setQuantidade( String.valueOf( item.getQuantidade() ) );		
	}	
	
	public ItemCompraResponse novoItemCompraResponse() {
		return new ItemCompraResponse();
	}
	
	public ItemCompra novoItemCompra() {
		ItemCompra item = new ItemCompra();
		item.setProduto( new Produto() );
		item.setCompra( new Compra() );			
		
		return item;
	}
	
}
