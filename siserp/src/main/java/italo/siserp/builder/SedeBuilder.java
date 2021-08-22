package italo.siserp.builder;

import org.springframework.stereotype.Component;

import italo.siserp.model.Sede;
import italo.siserp.service.request.SaveSedeRequest;
import italo.siserp.service.response.SedeResponse;

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

