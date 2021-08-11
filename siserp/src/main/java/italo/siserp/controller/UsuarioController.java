package italo.siserp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.siserp.exception.UsuarioJaExisteException;
import italo.siserp.exception.UsuarioNaoEncontradoException;
import italo.siserp.model.request.BuscaUsuariosRequest;
import italo.siserp.model.request.SaveUsuarioRequest;
import italo.siserp.model.response.ErroResponse;
import italo.siserp.model.response.UsuarioResponse;
import italo.siserp.service.UsuarioService;

@RestController
@RequestMapping(value="/api/usuario")
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;

	@PreAuthorize("hasAuthority('usuarioWRITE')")
	@PostMapping(value="/registra")
	public ResponseEntity<Object> registra( @RequestBody SaveUsuarioRequest req ) {
		if ( req.getUsername() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USERNAME_OBRIGATORIO ) );
		if ( req.getUsername().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USERNAME_OBRIGATORIO ) );		
		if ( req.getPassword() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PASSWORD_OBRIGATORIO ) );
		if ( req.getPassword().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PASSWORD_OBRIGATORIO ) );
		if ( req.getGrupo() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_GRUPO_OBRIGATORIO ) );
		if ( req.getGrupo().getNome().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_GRUPO_OBRIGATORIO ) );
		if ( req.getGrupo().getNome().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_GRUPO_OBRIGATORIO ) );

		try {
			usuarioService.registraUsuario( req );
			return ResponseEntity.ok().build();
		} catch (UsuarioJaExisteException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_JA_EXISTE ) );
		} 
	}
	
	@PreAuthorize("hasAuthority('usuarioWRITE')")
	@PutMapping(value="/atualiza/{usuarioId}")
	public ResponseEntity<Object> atualiza( @PathVariable Long usuarioId, @RequestBody SaveUsuarioRequest req ) {
		if ( req.getUsername() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USERNAME_OBRIGATORIO ) );
		if ( req.getUsername().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USERNAME_OBRIGATORIO ) );		
		if ( req.getPassword() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PASSWORD_OBRIGATORIO ) );
		if ( req.getPassword().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PASSWORD_OBRIGATORIO ) );
		if ( req.getGrupo() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_GRUPO_OBRIGATORIO ) );
		if ( req.getGrupo().getNome() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_GRUPO_OBRIGATORIO ) );
		if ( req.getGrupo().getNome().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_GRUPO_OBRIGATORIO ) );

		try {
			usuarioService.alteraUsuario( usuarioId, req );
			return ResponseEntity.ok().build();
		} catch (UsuarioJaExisteException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_JA_EXISTE ) );
		} catch (UsuarioNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_NAO_ENCONTRADO ) );
		} 
	}
	
	@PreAuthorize("hasAuthority('usuarioREAD')")
	@PostMapping(value="/filtra")
	public ResponseEntity<Object> filtra( @RequestBody BuscaUsuariosRequest request ) {
		if ( request.getUsernameIni() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USERNAME_OBRIGATORIO ) );
		if ( request.getUsernameIni().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USERNAME_OBRIGATORIO ) );		
		
		List<UsuarioResponse> lista = usuarioService.filtraUsuarios( request );
		return ResponseEntity.ok( lista );
	}
	
	@PreAuthorize("hasAuthority('usuarioREAD')")
	@GetMapping(value="/get/{usuarioId}")
	public ResponseEntity<Object> busca( @PathVariable Long usuarioId ) {				
		try {
			UsuarioResponse resp = usuarioService.buscaUsuario( usuarioId );
			return ResponseEntity.ok( resp );
		} catch (UsuarioNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_NAO_ENCONTRADO ) );
		}
	}
	
	@PreAuthorize("hasAuthority('usuarioDELETE')")
	@DeleteMapping(value="/deleta/{usuarioId}")
	public ResponseEntity<Object> deleta( @PathVariable Long usuarioId ) {
		try {
			usuarioService.deletaUsuario( usuarioId );
			return ResponseEntity.ok().build();
		} catch (UsuarioNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_NAO_ENCONTRADO ) );
		}
	}
	
}
