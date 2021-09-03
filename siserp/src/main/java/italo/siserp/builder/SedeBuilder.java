package italo.siserp.builder;

import org.springframework.stereotype.Component;

import italo.siserp.model.Sede;
import italo.siserp.model.request.SaveSedeRequest;
import italo.siserp.model.response.SedeResponse;

@Component
public class SedeBuilder {

	public void carregaSede( Sede sede, SaveSedeRequest req ) {
		sede.setCnpj( req.getCnpj() );
		sede.setInscricaoEstadual( req.getInscricaoEstadual() ); 
	}
		
	public void carregaSedeResponse( SedeResponse resp, Sede sede ) {
		resp.setCnpj( sede.getCnpj() );
		resp.setInscricaoEstadual( sede.getInscricaoEstadual() );
	}	
	
	public SedeResponse novoSedeResponse() {
		return new SedeResponse();
	}
	
	public Sede novoSede() {
		return new Sede();
	}
}

