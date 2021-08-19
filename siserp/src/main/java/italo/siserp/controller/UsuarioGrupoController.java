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

import italo.siserp.exception.TentativaDeletarGrupoNaoVazioException;
import italo.siserp.exception.UsuarioGrupoJaExisteException;
import italo.siserp.exception.UsuarioGrupoNaoEncontradoException;
import italo.siserp.service.UsuarioGrupoService;
import italo.siserp.service.request.BuscaUsuarioGruposRequest;
import italo.siserp.service.request.SaveUsuarioGrupoRequest;
import italo.siserp.service.response.ErroResponse;
import italo.siserp.service.response.UsuarioGrupoResponse;

@RestController
@RequestMapping(value="/api/usuario/grupo") 
public class UsuarioGrupoController {

	@Autowired
	private UsuarioGrupoService usuarioGrupoService;
		
	@PostMapping(value="/recursos/sincroniza/{grupoId}")
	public ResponseEntity<Object> sincronizaRecursos( @PathVariable Long grupoId ) {
		try {
			usuarioGrupoService.sincronizaRecursos( grupoId );
			return ResponseEntity.ok().build();
		} catch (UsuarioGrupoNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_GRUPO_NAO_ENCONTRADO ) );
		}
	}
	
	@PreAuthorize("hasAuthority('usuarioGrupoWRITE')")
	@PostMapping(value="/registra")
	public ResponseEntity<Object> registra( @RequestBody SaveUsuarioGrupoRequest req ) {
		if ( req.getNome() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.NOME_OBRIGATORIO ) );
		if ( req.getNome().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.NOME_OBRIGATORIO ) );		
		
		try {
			usuarioGrupoService.registraGrupo( req );
			return ResponseEntity.ok().build();
		} catch (UsuarioGrupoJaExisteException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_GRUPO_JA_EXISTE ) );
		} 
	}
	
	@PreAuthorize("hasAuthority('usuarioGrupoWRITE')")
	@PutMapping(value="/atualiza/{grupoId}")
	public ResponseEntity<Object> atualiza( @PathVariable Long grupoId, @RequestBody SaveUsuarioGrupoRequest req ) {
		if ( req.getNome() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USERNAME_OBRIGATORIO ) );
		if ( req.getNome().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USERNAME_OBRIGATORIO ) );		
		
		try {
			usuarioGrupoService.alteraGrupoInfo( grupoId, req );
			return ResponseEntity.ok().build();
		} catch (UsuarioGrupoJaExisteException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_GRUPO_JA_EXISTE ) );
		} catch (UsuarioGrupoNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_GRUPO_NAO_ENCONTRADO ) );
		} 
	}
		
	@GetMapping(value="/lista")
	public ResponseEntity<Object> buscaGrupos() {
		String[] grupos = usuarioGrupoService.listaGrupos();
		return ResponseEntity.ok( grupos );
	}
	
	@PreAuthorize("hasAuthority('usuarioGrupoREAD')")
	@PostMapping(value="/filtra")
	public ResponseEntity<Object> filtra( @RequestBody BuscaUsuarioGruposRequest request ) {
		if ( request.getNomeIni() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.NOME_OBRIGATORIO ) );
		if ( request.getNomeIni().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.NOME_OBRIGATORIO ) );		
		
		List<UsuarioGrupoResponse> lista = usuarioGrupoService.filtraGrupos( request );
		return ResponseEntity.ok( lista );
	}
	
	@PreAuthorize("hasAuthority('usuarioGrupoREAD')")
	@GetMapping(value="/get/{grupoId}")
	public ResponseEntity<Object> busca( @PathVariable Long grupoId ) {				
		try {
			UsuarioGrupoResponse resp = usuarioGrupoService.buscaGrupo( grupoId );
			return ResponseEntity.ok( resp );
		} catch (UsuarioGrupoNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_GRUPO_NAO_ENCONTRADO ) );
		}
	}
	
	@PreAuthorize("hasAuthority('usuarioGrupoDELETE')")
	@DeleteMapping(value="/deleta/{grupoId}")
	public ResponseEntity<Object> deleta( @PathVariable Long grupoId ) {
		try {
			usuarioGrupoService.deletaUsuarioGrupo( grupoId );
			return ResponseEntity.ok().build();
		} catch (UsuarioGrupoNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_GRUPO_NAO_ENCONTRADO ) );
		} catch (TentativaDeletarGrupoNaoVazioException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.TENTATIVA_DELETAR_GRUPO_NAO_VAZIO ) );
		}
	}	
	
}
