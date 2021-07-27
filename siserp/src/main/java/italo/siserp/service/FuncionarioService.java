package italo.siserp.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import italo.siserp.component.HashUtil;
import italo.siserp.component.builder.FuncionarioBuilder;
import italo.siserp.exception.FuncionarioNaoEncontradoException;
import italo.siserp.exception.PessoaJaExisteException;
import italo.siserp.exception.UsuarioJaExisteException;
import italo.siserp.model.Funcionario;
import italo.siserp.model.Pessoa;
import italo.siserp.model.Usuario;
import italo.siserp.model.request.BuscaFuncionariosRequest;
import italo.siserp.model.request.SaveFuncionarioRequest;
import italo.siserp.model.request.SaveUsuarioRequest;
import italo.siserp.model.response.FuncionarioResponse;
import italo.siserp.model.response.IdResponse;
import italo.siserp.repository.FuncionarioRepository;
import italo.siserp.repository.PessoaRepository;
import italo.siserp.repository.UsuarioRepository;

@Service
public class FuncionarioService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
			
	@Autowired
	private FuncionarioBuilder funcionarioBuilder;
	
	@Autowired
	private HashUtil hashUtil;

	public IdResponse registraFuncionario( SaveFuncionarioRequest request ) 
			throws PessoaJaExisteException, UsuarioJaExisteException {
		
		if ( this.existeNome( request.getPessoa().getNome() ) )
			throw new PessoaJaExisteException();
		
		if ( this.existeUsuario( request.getUsuario() ) )
			throw new UsuarioJaExisteException();
				
		Funcionario f = funcionarioBuilder.novoFuncionario();
		funcionarioBuilder.carregaFuncionario( f, request ); 
		
		funcionarioRepository.save( f );
					
		return new IdResponse( f.getId() ); 
	}
	
	public void atualizaFuncionario( Long id, SaveFuncionarioRequest req )
			throws FuncionarioNaoEncontradoException, 
					PessoaJaExisteException,
					UsuarioJaExisteException {
		
		Funcionario f = funcionarioRepository.findById( id ).orElseThrow( FuncionarioNaoEncontradoException::new );
		
		String nome = req.getPessoa().getNome();
		
		if ( !nome.equalsIgnoreCase( f.getPessoa().getNome() ) )
			if ( this.existeNome( nome ) )
				throw new PessoaJaExisteException();
		
		String username = req.getUsuario().getUsername();
		String password = hashUtil.geraHash( req.getUsuario().getPassword() );
		if ( !( username.equalsIgnoreCase( f.getUsuario().getUsername() ) || 
				password.equals( f.getUsuario().getPassword() ) ) ) {
			if ( this.existeUsuario( req.getUsuario() ) )
				throw new UsuarioJaExisteException();
		}
		
		funcionarioBuilder.carregaFuncionario( f, req );
		
		funcionarioRepository.save( f );
	}
	
	public List<FuncionarioResponse> buscaFuncionariosPorNomeIni( BuscaFuncionariosRequest request ) {
		String nomeIni = (request.getNomeIni().equals( "*" ) ? "" : request.getNomeIni() );
		String usernameIni = (request.getUsernameIni().equals( "*" ) ? "" : request.getUsernameIni() );
		
		List<Funcionario> funcionarios = funcionarioRepository.filtra(
				nomeIni+"%", 
				usernameIni+"%" );
		
		List<FuncionarioResponse> responses = new ArrayList<>();
		
		for( Funcionario f : funcionarios ) {			
			FuncionarioResponse resp = funcionarioBuilder.novoFuncionarioResponse();
			funcionarioBuilder.carregaFuncionarioResponse( resp, f );
			
			responses.add( resp );
		}
		
		return responses;
	}
	
	public FuncionarioResponse buscaFuncionarioPorId( Long id ) throws FuncionarioNaoEncontradoException {
		Funcionario f = funcionarioRepository.findById( id ).orElseThrow( FuncionarioNaoEncontradoException::new );
				
		FuncionarioResponse resp = funcionarioBuilder.novoFuncionarioResponse();
		funcionarioBuilder.carregaFuncionarioResponse( resp, f ); 
		
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
	
	public boolean existeUsuario( SaveUsuarioRequest usuario ) {
		Usuario u = new Usuario();
		u.setUsername( usuario.getUsername() ); 
		u.setPassword( hashUtil.geraHash( usuario.getPassword() ) );
		
		ExampleMatcher em = ExampleMatcher.matching()
				.withMatcher( "username", ExampleMatcher.GenericPropertyMatchers.ignoreCase() )
				.withMatcher( "password", ExampleMatcher.GenericPropertyMatchers.exact() );
		
		Example<Usuario> ex = Example.of( u, em );
		
		return usuarioRepository.exists( ex );
	}
	
	public void deleta( Long id ) throws FuncionarioNaoEncontradoException {
		if ( !funcionarioRepository.existsById( id ) )
			throw new FuncionarioNaoEncontradoException();
		
		funcionarioRepository.deleteById( id ); 
	}
		
}
