package italo.siserp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.siserp.exception.PermissaoEscritaException;
import italo.siserp.exception.PermissaoGrupoNaoEncontradoException;
import italo.siserp.exception.PermissaoLeituraException;
import italo.siserp.exception.PermissaoRemocaoException;
import italo.siserp.exception.PermissaoTipoInvalidoException;
import italo.siserp.service.PermissaoGrupoService;
import italo.siserp.service.request.SavePermissaoGrupoRequest;
import italo.siserp.service.request.SavePermissaoRequest;
import italo.siserp.service.response.ErroResponse;

@RestController
@RequestMapping(value="/api/permissao")
public class PermissaoGrupoController {

	@Autowired
	private PermissaoGrupoService permissaoGrupoService;
	
	@PreAuthorize("hasAuthority('permissaoGrupoWRITE')")
	@PostMapping(value="/grupo/salva/{id}")
	public ResponseEntity<Object> salvaPermissaoGrupo( @PathVariable Long id, @RequestBody SavePermissaoGrupoRequest request ) {
		try {
			permissaoGrupoService.salvaPermissaoGrupo( id, request );
			return 	ResponseEntity.ok().build();
		} catch (PermissaoGrupoNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PERMISSAO_GRUPO_NAO_ENCONTRADO ) );
		} catch (PermissaoLeituraException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PERMISSAO_LEITURA_INVALIDA ) );
		} catch (PermissaoEscritaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PERMISSAO_ESCRITA_INVALIDA ) );
		} catch (PermissaoRemocaoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PERMISSAO_REMOCAO_INVALIDA ) );
		}
	}
	
	@PreAuthorize("hasAuthority('permissaoGrupoWRITE')")
	@PatchMapping(value="/salva/{id}")
	public ResponseEntity<Object> salvaPermissao( @PathVariable Long id, @RequestBody SavePermissaoRequest request ) {
		try {
			permissaoGrupoService.salvaPermissao( id, request );
			return 	ResponseEntity.ok().build();
		} catch (PermissaoGrupoNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PERMISSAO_GRUPO_NAO_ENCONTRADO ) );
		} catch (PermissaoLeituraException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PERMISSAO_LEITURA_INVALIDA ) );
		} catch (PermissaoEscritaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PERMISSAO_ESCRITA_INVALIDA ) );
		} catch (PermissaoRemocaoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PERMISSAO_REMOCAO_INVALIDA ) );
		} catch (PermissaoTipoInvalidoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PERMISSAO_TIPO_INVALIDO ) );
		}
	}
	
}
