package italo.siserp.component.builder;

import org.springframework.stereotype.Component;

import italo.siserp.model.Fornecedor;
import italo.siserp.model.request.SaveFornecedorRequest;
import italo.siserp.model.response.FornecedorResponse;

@Component
public class FornecedorBuilder {

	public void carregaFornecedor( Fornecedor e, SaveFornecedorRequest req ) {
		e.setEmpresa( req.getEmpresa() ); 
	}
	
	public void carregaFornecedorResponse( FornecedorResponse resp, Fornecedor e ) {
		resp.setId( e.getId() );
		resp.setEmpresa( e.getEmpresa() );
	}	
	
	public FornecedorResponse novoFornecedorResponse() {
		return new FornecedorResponse();
	}
	
	public Fornecedor novoFornecedor() {
		return new Fornecedor();
	}
	
}

