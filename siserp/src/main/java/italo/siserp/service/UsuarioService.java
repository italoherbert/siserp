package italo.siserp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.siserp.component.HashUtil;
import italo.siserp.component.JwtTokenUtil;
import italo.siserp.component.builder.UsuarioBuilder;
import italo.siserp.exception.UsernameNaoEncontradoException;
import italo.siserp.exception.UsernamePasswordNaoCorrespondemException;
import italo.siserp.model.Usuario;
import italo.siserp.model.request.LoginRequest;
import italo.siserp.model.response.LoginResponse;
import italo.siserp.model.response.UsuarioResponse;
import italo.siserp.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private UsuarioBuilder usuarioCarregador;
	
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
		
		UsuarioResponse uResp = usuarioCarregador.novoUsuarioResponse();
		usuarioCarregador.carregaUsuarioResponse( uResp, u ); 
		
		String[] roles = {
			String.valueOf( u.getTipo() )
		};
				
		String token = tokenUtil.geraToken( request.getUsername(), roles );
		
		LoginResponse resp = new LoginResponse();
		resp.setUsuario( uResp );
		resp.setToken( token );
		return resp;
	}
	
}
