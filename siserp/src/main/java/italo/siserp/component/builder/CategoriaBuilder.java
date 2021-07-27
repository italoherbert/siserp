package italo.siserp.component.builder;

import org.springframework.stereotype.Component;

import italo.siserp.model.Categoria;
import italo.siserp.model.request.SaveCategoriaRequest;
import italo.siserp.model.response.CategoriaResponse;

@Component
public class CategoriaBuilder {

	public void carregaCategoria( Categoria c, SaveCategoriaRequest req ) {
		c.setDescricao( req.getDescricao() ); 
	}
	
	public void carregaCategoriaResponse( CategoriaResponse resp, Categoria c ) {
		resp.setId( c.getId() );
		resp.setDescricao( c.getDescricao() );
	}	
	
	public CategoriaResponse novoCategoriaResponse() {
		return new CategoriaResponse();
	}
	
	public Categoria novoCategoria() {
		return new Categoria();
	}
	
}

