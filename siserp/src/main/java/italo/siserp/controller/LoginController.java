package italo.siserp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.siserp.exception.UsernameNaoEncontradoException;
import italo.siserp.exception.UsernamePasswordNaoCorrespondemException;
import italo.siserp.service.LoginService;
import italo.siserp.service.request.LoginRequest;
import italo.siserp.service.response.ErroResponse;
import italo.siserp.service.response.LoginResponse;

@RestController
@RequestMapping(value="/api/login")
public class LoginController {

	@Autowired
	private LoginService loginService;
	
	@PostMapping(value="/entrar")
	public ResponseEntity<Object> entrar( @RequestBody LoginRequest request ) {
		if ( request.getUsername() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USERNAME_OBRIGATORIO ) );
		if ( request.getUsername().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USERNAME_OBRIGATORIO ) );
		
		if ( request.getPassword() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PASSWORD_OBRIGATORIO ) );
		if ( request.getPassword().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PASSWORD_OBRIGATORIO ) );
		
		try {
			LoginResponse resp = loginService.login( request );		
			return ResponseEntity.ok( resp );
		} catch ( UsernameNaoEncontradoException e ) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_NAO_ENCONTRADO ) );			
		} catch ( UsernamePasswordNaoCorrespondemException e ) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USERNAME_PASSWORD_NAO_CORRESPONDEM ) );						
		}
	}
	
}
