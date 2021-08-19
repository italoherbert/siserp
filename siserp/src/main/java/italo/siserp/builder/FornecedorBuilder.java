package italo.siserp.builder;

import org.springframework.stereotype.Component;

import italo.siserp.model.Fornecedor;
import italo.siserp.service.request.SaveFornecedorRequest;
import italo.siserp.service.response.FornecedorResponse;

@Component
public class FornecedorBuilder {

	public void carregaFornecedor( Fornecedor f, SaveFornecedorRequest req ) {		
		f.setEmpresa( req.getEmpresa() ); 
	}
	
	public void carregaFornecedorResponse( FornecedorResponse resp, Fornecedor f ) {
		resp.setId( f.getId() );
		resp.setEmpresa( f.getEmpresa() );
	}	
	
	public FornecedorResponse novoFornecedorResponse() {
		return new FornecedorResponse();
	}
	
	public Fornecedor novoFornecedor() {
		return new Fornecedor();
	}
	
}

