package italo.siserp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import italo.siserp.builder.ClienteBuilder;
import italo.siserp.exception.ClienteNaoEncontradoException;
import italo.siserp.exception.PessoaJaExisteException;
import italo.siserp.exception.UsuarioJaExisteException;
import italo.siserp.model.Cliente;
import italo.siserp.model.Pessoa;
import italo.siserp.model.request.BuscaClientesRequest;
import italo.siserp.model.request.SaveClienteRequest;
import italo.siserp.model.response.ClienteResponse;
import italo.siserp.model.response.IdResponse;
import italo.siserp.repository.ClienteRepository;
import italo.siserp.repository.PessoaRepository;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
				
	@Autowired
	private ClienteBuilder clienteBuilder;
	
	public IdResponse registraCliente( SaveClienteRequest request ) 
			throws PessoaJaExisteException, UsuarioJaExisteException {
		
		if ( this.existeNome( request.getPessoa().getNome() ) )
			throw new PessoaJaExisteException();
						
		Cliente f = clienteBuilder.novoCliente();
		clienteBuilder.carregaCliente( f, request ); 
		
		clienteRepository.save( f );
					
		return new IdResponse( f.getId() ); 
	}
	
	public void atualizaCliente( Long id, SaveClienteRequest req )
			throws ClienteNaoEncontradoException, 
					PessoaJaExisteException,
					UsuarioJaExisteException {
		
		Cliente f = clienteRepository.findById( id ).orElseThrow( ClienteNaoEncontradoException::new );
		
		String nome = req.getPessoa().getNome();
		
		if ( !nome.equalsIgnoreCase( f.getPessoa().getNome() ) )
			if ( this.existeNome( nome ) )
				throw new PessoaJaExisteException();
				
		clienteBuilder.carregaCliente( f, req );
		
		clienteRepository.save( f );
	}
	
	public List<ClienteResponse> buscaClientesPorNomeIni( BuscaClientesRequest request ) {
		String nomeIni = (request.getNomeIni().equals( "*" ) ? "" : request.getNomeIni() );
		
		List<Cliente> clientes = clienteRepository.filtra( nomeIni+"%" );
		
		List<ClienteResponse> responses = new ArrayList<>();
		
		for( Cliente f : clientes ) {			
			ClienteResponse resp = clienteBuilder.novoClienteResponse();
			clienteBuilder.carregaClienteResponse( resp, f );
			
			responses.add( resp );
		}
		
		return responses;
	}
	
	public ClienteResponse buscaClientePorId( Long id ) throws ClienteNaoEncontradoException {
		Cliente f = clienteRepository.findById( id ).orElseThrow( ClienteNaoEncontradoException::new );
				
		ClienteResponse resp = clienteBuilder.novoClienteResponse();
		clienteBuilder.carregaClienteResponse( resp, f ); 
		
		return resp;
	}
	
	public boolean existeNome( String nome ) {
		Pessoa p = new Pessoa();
		p.setNome( nome );
		
		ExampleMatcher em = ExampleMatcher.matching()
				.withMatcher( "nome", ExampleMatcher.GenericPropertyMatchers.ignoreCase() );
		
		Example<Pessoa> ex = Example.of( p, em );
		
		return pessoaRepository.exists( ex );
	}
		
	public void deleta( Long id ) throws ClienteNaoEncontradoException {
		if ( !clienteRepository.existsById( id ) )
			throw new ClienteNaoEncontradoException();
		
		clienteRepository.deleteById( id ); 
	}
		
}

