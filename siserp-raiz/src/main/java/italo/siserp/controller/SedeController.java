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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.siserp.exception.SedeJaExisteException;
import italo.siserp.exception.SedeNaoEncontradaException;
import italo.siserp.model.request.BuscaSedesRequest;
import italo.siserp.model.request.SaveSedeRequest;
import italo.siserp.model.response.ErroResponse;
import italo.siserp.model.response.IdResponse;
import italo.siserp.model.response.SedeResponse;
import italo.siserp.service.SedeService;

@RestController
@RequestMapping(value="/api/sede")
public class SedeController {
	
	@Autowired
	private SedeService sedeService;
		
	@PreAuthorize("hasAuthority('RAIZ')")	
	@PostMapping(value="/registra/{empresaId}")
	public ResponseEntity<Object> registra( @PathVariable Long empresaId, @RequestBody SaveSedeRequest request ) {
		if ( request.getCnpj() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CNPJ_OBRIGATORIO ) );
		if ( request.getCnpj().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CNPJ_OBRIGATORIO ) );
		
		if ( request.getInscricaoEstadual() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.INSCRICAO_ESTADUAL_OBRIGATORIA ) );
		if ( request.getInscricaoEstadual().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.INSCRICAO_ESTADUAL_OBRIGATORIA ) );
		
		try {
			IdResponse resp = sedeService.registraSede( empresaId, request );
			return ResponseEntity.ok( resp );
		} catch (SedeJaExisteException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.SEDE_JA_EXISTE ) );
		}
	}
	
	@PreAuthorize("hasAnyAuthority('RAIZ', 'ADMIN')")
	@PutMapping(value="/atualiza/{id}")
	public ResponseEntity<Object> atualiza( @PathVariable Long id, @RequestBody SaveSedeRequest request ) {
		
		if ( request.getCnpj() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CNPJ_OBRIGATORIO ) );
		if ( request.getCnpj().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CNPJ_OBRIGATORIO ) );
		
		if ( request.getInscricaoEstadual() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.INSCRICAO_ESTADUAL_OBRIGATORIA ) );
		if ( request.getInscricaoEstadual().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.INSCRICAO_ESTADUAL_OBRIGATORIA ) );
		
		try {
			sedeService.atualizaSede( id, request );
			return ResponseEntity.ok().build();
		} catch (SedeJaExisteException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.SEDE_JA_EXISTE ) );
		} catch (SedeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.SEDE_NAO_ENCONTRADA ) );
		}
	}
	
	@PreAuthorize("hasAuthority('RAIZ')")	
	@PostMapping(value="/filtra")
	public ResponseEntity<Object> filtra( @RequestBody BuscaSedesRequest request ) {		
		List<SedeResponse> resps = sedeService.filtraSedes( request );
		return ResponseEntity.ok( resps );	
	}
	
	@PreAuthorize("hasAnyAuthority('RAIZ', 'ADMIN', 'GERENTE', 'CAIXA')")	
	@GetMapping(value="/get/{id}")
	public ResponseEntity<Object> get( @RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id ) {		
		try {
			SedeResponse resp = sedeService.getSede( id );
			return ResponseEntity.ok( resp );	
		} catch (SedeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.SEDE_NAO_ENCONTRADA ) );
		}
	}
	
	@PreAuthorize("hasAnyAuthority('RAIZ')")	
	@DeleteMapping(value="/deleta/{id}")
	public ResponseEntity<Object> deleta( @PathVariable Long id ) {		
		try {
			sedeService.deletaSede( id );
			return ResponseEntity.ok().build();	
		} catch (SedeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.SEDE_NAO_ENCONTRADA ) );
		}
	}
	
}

