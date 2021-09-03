package italo.siserp.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.model.Cliente;
import italo.siserp.model.Endereco;
import italo.siserp.model.Pessoa;
import italo.siserp.model.request.SaveClienteRequest;
import italo.siserp.model.response.ClienteResponse;

@Component
public class ClienteBuilder {

	@Autowired
	private PessoaBuilder pessoaBuilder;
			
	public void carregaCliente( Cliente c, SaveClienteRequest req ) {
		pessoaBuilder.carregaPessoa( c.getPessoa(), req.getPessoa() );
	}
	
	public void carregaClienteResponse( ClienteResponse resp, Cliente c ) {
		resp.setId( c.getId() );		
		pessoaBuilder.carregaPessoaResponse( resp.getPessoa(), c.getPessoa() );
	}
	
	public ClienteResponse novoClienteResponse() {		
		ClienteResponse resp = new ClienteResponse();
		resp.setPessoa( pessoaBuilder.novoPessoaResponse() );
		return resp;
	}
	
	public Cliente novoCliente() {
		Cliente c = new Cliente();
		c.setPessoa( new Pessoa() );
		
		c.getPessoa().setEndereco( new Endereco() );
		return c;
	}
			
}

