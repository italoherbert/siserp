package italo.siserp.component.builder;

import org.springframework.stereotype.Component;

import italo.siserp.model.Empresa;
import italo.siserp.model.request.SaveEmpresaRequest;
import italo.siserp.model.response.EmpresaResponse;

@Component
public class EmpresaBuilder {
	
	public void carregaEmpresa( Empresa e, SaveEmpresaRequest req ) {
		e.setRazaoSocial( req.getRazaoSocial() );
	}
	
	public void carregaEmpresaResponse( EmpresaResponse resp, Empresa e ) {
		resp.setId( e.getId() );
		resp.setRazaoSocial( e.getRazaoSocial() );
	}	
	
	public EmpresaResponse novoEmpresaResponse() {
		return new EmpresaResponse();
	}
	
	public Empresa novoEmpresa() {
		return new Empresa();
	}
	
}

