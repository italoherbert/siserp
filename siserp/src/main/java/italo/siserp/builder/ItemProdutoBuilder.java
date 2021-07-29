package italo.siserp.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.exception.QuantidadeInvalidaException;
import italo.siserp.model.CategoriaMap;
import italo.siserp.model.ItemProduto;
import italo.siserp.model.request.SaveItemProdutoRequest;
import italo.siserp.model.response.CategoriaResponse;
import italo.siserp.model.response.ItemProdutoResponse;

@Component
public class ItemProdutoBuilder {

	@Autowired
	private CategoriaBuilder categoriaBuilder;
		
	public void carregaItemProduto( ItemProduto ip, SaveItemProdutoRequest req ) 
			throws QuantidadeInvalidaException {		
		try {
			ip.setQuantidade( ip.getQuantidade() + Double.parseDouble( req.getQuantidade() ) );
		} catch ( NumberFormatException e ) {
			QuantidadeInvalidaException ex = new QuantidadeInvalidaException();
			ex.setParams( req.getQuantidade() );
			throw ex;
		}				
	}
	
	public void carregaItemProdutoResponse( ItemProdutoResponse resp, ItemProduto ip ) {
		resp.setId( ip.getId() );
		resp.setQuantidade( String.valueOf( ip.getQuantidade() ) ); 
		
		List<CategoriaResponse> categorias = new ArrayList<>();
		for( CategoriaMap map : ip.getCategoriaMaps() ) {
			CategoriaResponse cresp = categoriaBuilder.novoCategoriaResponse();
			categoriaBuilder.carregaCategoriaResponse( cresp, map.getCategoria() );
			categorias.add( cresp );
		}
		resp.setCategorias( categorias );				
	}	
	
	public ItemProdutoResponse novoItemProdutoResponse() {
		return new ItemProdutoResponse();
	}
	
	public ItemProduto novoItemProduto() {
		return new ItemProduto();
	}
	
}
