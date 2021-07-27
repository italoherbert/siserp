package italo.siserp.component.builder;

import org.springframework.stereotype.Component;

import italo.siserp.model.Empresa;
import italo.siserp.model.Sede;
import italo.siserp.model.request.SaveSedeRequest;
import italo.siserp.model.response.EmpresaResponse;
import italo.siserp.model.response.SedeResponse;

@Component
public class SedeBuilder {
	
	private EmpresaBuilder empresaBuilder;
	
	public void carregaSede( Sede e, SaveSedeRequest req ) {
		e.setInscricaoEstadual( req.getInscricaoEstadual() );
		e.setCnpj( req.getCnpj() );		
	}
	
	public void carregaSedeResponse( SedeResponse resp, Sede e ) {		
		resp.setId( e.getId() );
		resp.setInscricaoEstadual( e.getInscricaoEstadual() );
		resp.setCnpj( e.getCnpj() ); 		
		empresaBuilder.carregaEmpresaResponse( resp.getEmpresa(), e.getEmpresa() ); 
	}	
	
	public SedeResponse novoSedeResponse() {
		SedeResponse resp = new SedeResponse();
		resp.setEmpresa( new EmpresaResponse() ); 
		return resp;
	}
	
	public Sede novoSede( Long empresaId ) {
		Sede sede = new Sede();
		sede.setEmpresa( new Empresa() );
		
		sede.getEmpresa().setId( empresaId );
		return new Sede();
	}
	
}
