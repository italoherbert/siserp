package italo.siserp.builder;

import org.springframework.stereotype.Component;

import italo.siserp.model.Recurso;
import italo.siserp.model.request.SaveRecursoRequest;
import italo.siserp.model.response.RecursoResponse;

@Component
public class RecursoBuilder {

	public void carregaRecurso( Recurso r, SaveRecursoRequest req ) {		
		r.setNome( req.getNome() ); 
	}
	
	public void carregaRecursoResponse( RecursoResponse resp, Recurso r ) {
		resp.setId( r.getId() );
		resp.setNome( r.getNome() );
	}	
	
	public RecursoResponse novoRecursoResponse() {
		return new RecursoResponse();
	}
	
	public Recurso novoRecurso() {
		return new Recurso();
	}
	
}