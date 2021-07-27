package italo.siserp.component.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.model.Pessoa;
import italo.siserp.model.request.SavePessoaRequest;
import italo.siserp.model.response.PessoaResponse;

@Component
public class PessoaBuilder {
	
	@Autowired
	private EnderecoBuilder enderecoBuilder;
	
	public void carregaPessoa( Pessoa p, SavePessoaRequest req ) {
		p.setNome( req.getNome() );
		p.setTelefone( req.getTelefone() );
		p.setEmail( req.getEmail() ); 
		enderecoBuilder.carregaEndereco( p.getEndereco(), req.getEndereco() ); 
	}
	
	public void carregaPessoaResponse( PessoaResponse resp, Pessoa p ) {
		resp.setId( p.getId() );
		resp.setNome( p.getNome() );
		resp.setTelefone( p.getTelefone() );
		resp.setEmail( p.getEmail() );
		enderecoBuilder.carregaEnderecoResponse( resp.getEndereco(), p.getEndereco() ); 
	}
	
	public PessoaResponse novoPessoaResponse() {
		PessoaResponse resp = new PessoaResponse();
		resp.setEndereco( enderecoBuilder.novoEnderecoResponse() );
		return resp;
	}
	
	public Pessoa novoPessoa() {
		Pessoa p = new Pessoa();
		p.setEndereco( enderecoBuilder.novoEndereco() );
		return p;
	}
	
}
