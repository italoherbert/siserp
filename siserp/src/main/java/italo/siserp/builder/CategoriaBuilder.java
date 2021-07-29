package italo.siserp.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.model.Categoria;
import italo.siserp.model.SubCategoria;
import italo.siserp.model.request.SaveCategoriaRequest;
import italo.siserp.model.response.CategoriaResponse;
import italo.siserp.model.response.SubCategoriaResponse;

@Component
public class CategoriaBuilder {

	@Autowired
	private SubCategoriaBuilder subcategoriaBuilder;
	
	public void carregaCategoria( Categoria c, SaveCategoriaRequest req ) {
		c.setDescricao( req.getDescricao() ); 
	}
	
	public void carregaCategoriaResponse( CategoriaResponse resp, Categoria c ) {
		resp.setId( c.getId() );
		resp.setDescricao( c.getDescricao() );
		
		List<SubCategoriaResponse> lista = new ArrayList<>();
		for( SubCategoria sc : c.getSubcategorias() ) {
			SubCategoriaResponse scresp = subcategoriaBuilder.novoSubCategoriaResponse();
			subcategoriaBuilder.carregaSubCategoriaResponse( scresp, sc );			
			lista.add( scresp );
		}
		
		resp.setSubcategorias( lista);
	}	
	
	public CategoriaResponse novoCategoriaResponse() {		
		return new CategoriaResponse();
	}
	
	public Categoria novoCategoria() {
		return new Categoria();
	}
	
}

