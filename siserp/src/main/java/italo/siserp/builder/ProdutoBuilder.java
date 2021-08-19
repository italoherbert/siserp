package italo.siserp.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.exception.DoubleInvalidoException;
import italo.siserp.exception.PrecoUnitCompraInvalidoException;
import italo.siserp.exception.PrecoUnitVendaInvalidoException;
import italo.siserp.exception.QuantidadeInvalidaException;
import italo.siserp.model.Categoria;
import italo.siserp.model.CategoriaMap;
import italo.siserp.model.Produto;
import italo.siserp.model.SubCategoria;
import italo.siserp.service.request.SaveProdutoRequest;
import italo.siserp.service.response.CategoriaResponse;
import italo.siserp.service.response.ProdutoResponse;
import italo.siserp.service.response.SubCategoriaResponse;
import italo.siserp.util.NumeroUtil;

@Component
public class ProdutoBuilder {
	
	@Autowired
	private CategoriaBuilder categoriaBuilder;	
	
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
			p.setQuantidade( p.getQuantidade() + numeroUtil.stringParaDouble( req.getQuantidade() ) );
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
				
		List<CategoriaResponse> categorias = new ArrayList<>();
		for( CategoriaMap map : p.getCategoriaMaps() ) {
			Categoria c = map.getCategoria();
			SubCategoria sc = map.getSubcategoria();
			
			String catDesc = c.getDescricao();
			String subcatDesc = sc.getDescricao();
			
			CategoriaResponse cresp = null;
			int size = categorias.size();
			for( int i = 0; resp == null && i < size; i++ ) {
				CategoriaResponse cresp2 = categorias.get( i );
				if ( cresp2.getDescricao().equalsIgnoreCase( catDesc ) )
					cresp = cresp2;
			}
			
			if ( cresp == null ) {
				cresp = categoriaBuilder.novoCategoriaResponse();												
				cresp.setId( c.getId() );
				cresp.setDescricao( catDesc );
				cresp.setSubcategorias( new ArrayList<>() );
				
				categorias.add( cresp );
			}
			
			SubCategoriaResponse scResp = new SubCategoriaResponse();
			scResp.setId( sc.getId() );
			scResp.setDescricao( subcatDesc );
			
			cresp.getSubcategorias().add( scResp );
		}
		
		resp.setCategorias( categorias );
	}	
	
	public ProdutoResponse novoProdutoResponse() {
		return new ProdutoResponse();
	}
	
	public Produto novoProduto() {		
		return new Produto();		
	}
	
}

