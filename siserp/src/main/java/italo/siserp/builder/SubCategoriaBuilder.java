package italo.siserp.builder;

import org.springframework.stereotype.Component;

import italo.siserp.model.SubCategoria;
import italo.siserp.service.request.SaveSubCategoriaRequest;
import italo.siserp.service.response.SubCategoriaResponse;

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
	
	public SubCategoria novoSubCategoria() {				
		return new SubCategoria();		
	}
	
}
