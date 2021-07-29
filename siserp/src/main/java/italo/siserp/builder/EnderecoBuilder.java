package italo.siserp.builder;

import org.springframework.stereotype.Component;

import italo.siserp.model.Endereco;
import italo.siserp.model.request.SaveEnderecoRequest;
import italo.siserp.model.response.EnderecoResponse;

@Component
public class EnderecoBuilder {

	public void carregaEndereco( Endereco e, SaveEnderecoRequest req ) {
		e.setEnder( req.getEnder() );
		e.setNumero( req.getNumero() );
		e.setBairro( req.getBairro() );
		e.setCidade( req.getCidade() );
		e.setUf( req.getUf() );
		e.setLogradouro( req.getLogradouro() ); 
	}
	
	public void carregaEnderecoResponse( EnderecoResponse resp, Endereco e ) {
		resp.setId( e.getId() );
		resp.setEnder( e.getEnder() );
		resp.setNumero( e.getNumero() );
		resp.setBairro( e.getBairro() );
		resp.setCidade( e.getCidade() );
		resp.setUf( e.getUf() );
		resp.setLogradouro( e.getLogradouro() ); 
	}	
	
	public EnderecoResponse novoEnderecoResponse() {
		return new EnderecoResponse();
	}
	
	public Endereco novoEndereco() {
		return new Endereco();
	}
	
}
