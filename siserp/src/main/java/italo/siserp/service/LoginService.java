package italo.siserp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.siserp.builder.UsuarioBuilder;
import italo.siserp.exception.UsernameNaoEncontradoException;
import italo.siserp.exception.UsernamePasswordNaoCorrespondemException;
import italo.siserp.model.PermissaoGrupo;
import italo.siserp.model.Usuario;
import italo.siserp.model.request.LoginRequest;
import italo.siserp.model.response.LoginResponse;
import italo.siserp.model.response.UsuarioResponse;
import italo.siserp.repository.UsuarioRepository;
import italo.siserp.util.HashUtil;
import italo.siserp.util.JwtTokenUtil;

@Service
public class LoginService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
			
	@Autowired
	private UsuarioBuilder usuarioBuilder;
		
	@Autowired
	private HashUtil hashUtil;
	
	@Autowired
	private JwtTokenUtil tokenUtil;				

	public LoginResponse login( LoginRequest request ) 
			throws UsernameNaoEncontradoException, 
				UsernamePasswordNaoCorrespondemException {
		
		String username = request.getUsername();
		String password = hashUtil.geraHash( request.getPassword() );
		
		Usuario u = usuarioRepository.findByUsername( username ).orElseThrow( UsernameNaoEncontradoException::new );				
		if ( !u.getPassword().equals( password ) )
			throw new UsernamePasswordNaoCorrespondemException();
		
		UsuarioResponse uResp = usuarioBuilder.novoUsuarioResponse();
		usuarioBuilder.carregaUsuarioResponse( uResp, u ); 
		
		List<PermissaoGrupo> permissaoGrupos = u.getGrupo().getPermissaoGrupos();
		List<String> authoritiesLista = new ArrayList<>();
		
		int size = permissaoGrupos.size();
		for( int i = 0; i < size; i++ ) {
			PermissaoGrupo p = permissaoGrupos.get( i );
			String recurso = p.getRecurso().getNome();
			if ( p.isEscrita() )
				authoritiesLista.add( recurso + PermissaoGrupo.PREFIXO_ESCRITA );
			
			if ( p.isLeitura() )
				authoritiesLista.add( recurso + PermissaoGrupo.PREFIXO_LEITURA );
			
			if ( p.isRemocao() )
				authoritiesLista.add( recurso + PermissaoGrupo.PREFIXO_REMOCAO );
		}
		
		authoritiesLista.add( "loginREAD" );
		
		String[] authorities = authoritiesLista.toArray( new String[ authoritiesLista.size() ] );				
		String token = tokenUtil.geraToken( request.getUsername(), authorities );
		
		LoginResponse resp = new LoginResponse();
		resp.setUsuario( uResp );
		resp.setToken( token );
		return resp;
	}
	
	
}
