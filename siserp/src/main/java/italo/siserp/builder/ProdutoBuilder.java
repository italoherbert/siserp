package italo.siserp.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.exception.PrecoUnitCompraInvalidoException;
import italo.siserp.exception.PrecoUnitVendaInvalidoException;
import italo.siserp.exception.QuantidadeInvalidaException;
import italo.siserp.model.ItemProduto;
import italo.siserp.model.Produto;
import italo.siserp.model.request.SaveItemProdutoRequest;
import italo.siserp.model.request.SaveProdutoRequest;
import italo.siserp.model.response.ProdutoResponse;

@Component
public class ProdutoBuilder {

	@Autowired
	private ItemProdutoBuilder itemProdutoBuilder;
	
	public void carregaProduto( Produto p, SaveProdutoRequest req ) 
			throws PrecoUnitCompraInvalidoException, 
				PrecoUnitVendaInvalidoException, 
				QuantidadeInvalidaException {
		
		p.setDescricao( req.getDescricao() );
		
		try {
			p.setPrecoUnitarioCompra( Double.parseDouble( req.getPrecoUnitCompra() ) );
		} catch ( NumberFormatException e ) {
			PrecoUnitCompraInvalidoException ex = new PrecoUnitCompraInvalidoException();
			ex.setParams( req.getPrecoUnitCompra() );
			throw ex;
		}
		
		try {
			p.setPrecoUnitarioVenda( Double.parseDouble( req.getPrecoUnitVenda() ) ); 
		} catch ( NumberFormatException e ) {
			PrecoUnitVendaInvalidoException ex = new PrecoUnitVendaInvalidoException();
			ex.setParams( req.getPrecoUnitVenda() );
			throw ex;
		}
				
		p.setUnidade( req.getUnidade() );
		p.setCodigoBarras( req.getCodigoBarras() );
		
		List<ItemProduto> iprods = new ArrayList<>(); 
		for( SaveItemProdutoRequest iprodreq : req.getItensProdutos() ) {
			ItemProduto ip = new ItemProduto();
			itemProdutoBuilder.carregaItemProduto( ip, iprodreq );
			
			iprods.add( ip );
		}
		p.setItensProdutos( iprods );
	}
	
	public void carregaProdutoResponse( ProdutoResponse resp, Produto p ) {
		resp.setId( p.getId() );
		resp.setDescricao( p.getDescricao() );
		resp.setPrecoUnitCompra( String.valueOf( p.getPrecoUnitarioCompra() ) );
		resp.setPrecoUnitVenda( String.valueOf( p.getPrecoUnitarioVenda() ) );
		resp.setUnidade( p.getUnidade() );
		resp.setCodigoBarras( p.getCodigoBarras() ); 
	}	
	
	public ProdutoResponse novoProdutoResponse() {
		return new ProdutoResponse();
	}
	
	public Produto novoProduto() {
		return new Produto();
	}
	
}

