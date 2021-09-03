package italo.siserp.builder;

import org.springframework.stereotype.Component;

import italo.siserp.model.CategoriaMap;
import italo.siserp.service.request.SaveCategoriaMapRequest;
import italo.siserp.service.response.CategoriaMapResponse;

@Component
public class CategoriaMapBuilder {
	
	public void carregaCategoriaMap( CategoriaMap map, SaveCategoriaMapRequest req ) {
		map.setCategoria( req.getCategoria() );
		map.setSubcategoria( req.getSubcategoria() ); 
	}
	
	public void carregaCategoriaMapResponse( CategoriaMapResponse resp, CategoriaMap c ) {
		resp.setId( c.getId() );
		resp.setCategoria( c.getCategoria() );
		resp.setSubcategoria( c.getSubcategoria() );				
	}	
	
	public CategoriaMapResponse novoCategoriaMapResponse() {		
		return new CategoriaMapResponse();
	}
	
	public CategoriaMap novoCategoriaMap() {
		return new CategoriaMap();
	}
	
}

