package italo.siserp.component.builder;

import org.springframework.stereotype.Component;

import italo.siserp.model.Categoria;
import italo.siserp.model.SubCategoria;
import italo.siserp.model.request.SaveSubCategoriaRequest;
import italo.siserp.model.response.SubCategoriaResponse;

@Component
public class SubCategoriaBuilder {

	public void carregaSubCategoria( SubCategoria sc, SaveSubCategoriaRequest req ) {
		sc.setDescricao( req.getDescricao() ); 
	}
	
	public void carregaSubCategoriaResponse( SubCategoriaResponse resp, SubCategoria sc ) {
		resp.setId( sc.getId() );
		resp.setDescricao( sc.getDescricao() );
	}	
	
	public SubCategoriaResponse novoSubCategoriaResponse() {
		return new SubCategoriaResponse();
	}
	
	public SubCategoria novoSubCategoria( Long categoriaId ) {				
		SubCategoria sc = new SubCategoria();
		sc.setCategoria( new Categoria() );
		
		sc.getCategoria().setId( categoriaId ); 
		return sc;
	}
	
}
