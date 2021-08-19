package italo.siserp.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.exception.DoubleInvalidoException;
import italo.siserp.exception.PrecoUnitCompraInvalidoException;
import italo.siserp.exception.PrecoUnitVendaInvalidoException;
import italo.siserp.exception.QuantidadeInvalidaException;
import italo.siserp.model.Compra;
import italo.siserp.model.ItemCompra;
import italo.siserp.model.Produto;
import italo.siserp.service.request.SaveItemCompraRequest;
import italo.siserp.service.response.ItemCompraResponse;
import italo.siserp.util.NumeroUtil;

@Component
public class ItemCompraBuilder {
		
	@Autowired
	private NumeroUtil numeroUtil;
	
	@Autowired
	private ProdutoBuilder produtoBuilder;
	
	public void carregaItemCompra( ItemCompra item, SaveItemCompraRequest req ) 
			throws PrecoUnitCompraInvalidoException,
				PrecoUnitVendaInvalidoException,
				QuantidadeInvalidaException {
		try {
			item.setPrecoUnitario( numeroUtil.stringParaDouble( req.getPrecoUnitario() ) );
		} catch ( DoubleInvalidoException e ) {
			PrecoUnitCompraInvalidoException ex = new PrecoUnitCompraInvalidoException();
			ex.setParams( req.getPrecoUnitario() );
			throw ex;
		}
		
		try {
			item.setQuantidade( numeroUtil.stringParaDouble( req.getQuantidade() ) ); 
		} catch ( DoubleInvalidoException e ) {
			QuantidadeInvalidaException ex = new QuantidadeInvalidaException();
			ex.setParams( req.getPrecoUnitario() );
			throw ex;
		}					
	}
	
	public void carregaItemCompraResponse( ItemCompraResponse resp, ItemCompra item ) {
		resp.setId( item.getId() ); 
		resp.setPrecoUnitario( numeroUtil.doubleParaString( item.getPrecoUnitario() ) );
		resp.setQuantidade( numeroUtil.doubleParaString( item.getQuantidade() ) );
		
		produtoBuilder.carregaProdutoResponse( resp.getProduto(), item.getProduto() ); 
	}	
	
	public ItemCompraResponse novoItemCompraResponse() {
		ItemCompraResponse resp = new ItemCompraResponse();
		resp.setProduto( produtoBuilder.novoProdutoResponse() );
		return resp;
	}
	
	public ItemCompra novoItemCompra() {
		ItemCompra item = new ItemCompra();
		item.setProduto( new Produto() );
		item.setCompra( new Compra() );			
		
		return item;
	}
	
}
