package italo.siserp.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.exception.DoubleInvalidoException;
import italo.siserp.exception.PrecoUnitCompraInvalidoException;
import italo.siserp.exception.PrecoUnitVendaInvalidoException;
import italo.siserp.exception.QuantidadeInvalidaException;
import italo.siserp.model.CategoriaMap;
import italo.siserp.model.Produto;
import italo.siserp.service.request.SaveProdutoRequest;
import italo.siserp.service.response.CategoriaMapResponse;
import italo.siserp.service.response.ProdutoResponse;
import italo.siserp.util.NumeroUtil;

@Component
public class ProdutoBuilder {
		
	@Autowired
	private NumeroUtil numeroUtil;
	
	public void carregaProduto( Produto p, SaveProdutoRequest req ) 
			throws PrecoUnitCompraInvalidoException, 
				PrecoUnitVendaInvalidoException, 
				QuantidadeInvalidaException {
		
		p.setDescricao( req.getDescricao() );
		p.setCodigoBarras( req.getCodigoBarras() );
		
		try {
			p.setPrecoUnitarioCompra( numeroUtil.stringParaDouble( req.getPrecoUnitCompra() ) );
		} catch ( DoubleInvalidoException e ) {
			PrecoUnitCompraInvalidoException ex = new PrecoUnitCompraInvalidoException();
			ex.setParams( req.getPrecoUnitCompra() );
			throw ex;
		}
		
		try {
			p.setPrecoUnitarioVenda( numeroUtil.stringParaDouble( req.getPrecoUnitVenda() ) ); 
		} catch ( DoubleInvalidoException e ) {
			PrecoUnitVendaInvalidoException ex = new PrecoUnitVendaInvalidoException();
			ex.setParams( req.getPrecoUnitVenda() );
			throw ex;
		}
		
		try {
			double quant = 0;
			if ( req.getQuantidade() != null )
				if ( !req.getQuantidade().isBlank() )
					quant = numeroUtil.stringParaDouble( req.getQuantidade() );
			
			p.setQuantidade( p.getQuantidade() + quant );
		} catch ( DoubleInvalidoException e ) {
			QuantidadeInvalidaException ex = new QuantidadeInvalidaException();
			ex.setParams( req.getQuantidade() );
			throw ex;
		}
				
		p.setUnidade( req.getUnidade() );
	}
	
	public void carregaProdutoResponse( ProdutoResponse resp, Produto p ) {
		resp.setId( p.getId() );
		resp.setDescricao( p.getDescricao() );
		resp.setCodigoBarras( p.getCodigoBarras() ); 
		resp.setPrecoUnitCompra( numeroUtil.doubleParaString( p.getPrecoUnitarioCompra() ) );
		resp.setPrecoUnitVenda( numeroUtil.doubleParaString( p.getPrecoUnitarioVenda() ) );
		resp.setUnidade( p.getUnidade() );
		
		resp.setQuantidade( numeroUtil.doubleParaString( p.getQuantidade() ) );
				
		List<CategoriaMapResponse> maps = new ArrayList<>();
		for( CategoriaMap map : p.getCategoriaMaps() ) {
			String c = map.getCategoria();
			String sc = map.getSubcategoria();
						
			CategoriaMapResponse mapresp = new CategoriaMapResponse();
			mapresp.setCategoria( c );
			mapresp.setSubcategoria( sc );
			
			maps.add( mapresp );			
		}		
		
		resp.setCategoriaMaps( maps ); 				
	}	
	
	public ProdutoResponse novoProdutoResponse() {
		return new ProdutoResponse();
	}
	
	public Produto novoProduto() {		
		return new Produto();		
	}
	
}

