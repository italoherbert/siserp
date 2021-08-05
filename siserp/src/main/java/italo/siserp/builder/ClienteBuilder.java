package italo.siserp.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.model.Cliente;
import italo.siserp.model.Endereco;
import italo.siserp.model.Pessoa;
import italo.siserp.model.request.SaveClienteRequest;
import italo.siserp.model.response.ClienteResponse;
import italo.siserp.model.response.EnderecoResponse;
import italo.siserp.model.response.PessoaResponse;

@Component
public class ClienteBuilder {

	@Autowired
	private PessoaBuilder pessoaBuilder;
			
	public void carregaCliente( Cliente f, SaveClienteRequest req ) {
		pessoaBuilder.carregaPessoa( f.getPessoa(), req.getPessoa() );
	}
	
	public void carregaClienteResponse( ClienteResponse resp, Cliente f ) {
		resp.setId( f.getId() );		
		pessoaBuilder.carregaPessoaResponse( resp.getPessoa(), f.getPessoa() );
	}
	
	public ClienteResponse novoClienteResponse() {		
		ClienteResponse resp = new ClienteResponse();
		resp.setPessoa( new PessoaResponse() );
		resp.getPessoa().setEndereco( new EnderecoResponse() );
		return resp;
	}
	
	public Cliente novoCliente() {
		Cliente f = new Cliente();
		f.setPessoa( new Pessoa() );
		
		f.getPessoa().setEndereco( new Endereco() );
		return f;
	}
			
}

