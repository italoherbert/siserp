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

import italo.siserp.exception.RecursoJaExisteException;
import italo.siserp.exception.RecursoNaoEncontradoException;
import italo.siserp.service.RecursoService;
import italo.siserp.service.request.BuscaRecursosRequest;
import italo.siserp.service.request.SaveRecursoRequest;
import italo.siserp.service.response.ErroResponse;
import italo.siserp.service.response.RecursoResponse;

@RestController
@RequestMapping(value="/api/recurso") 
public class RecursoController {

	@Autowired
	private RecursoService recursoService;
		
	@PreAuthorize("hasAuthority('recursoWRITE')")
	@PostMapping(value="/registra")
	public ResponseEntity<Object> registra( @RequestBody SaveRecursoRequest req ) {
		if ( req.getNome() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.NOME_OBRIGATORIO ) );
		if ( req.getNome().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.NOME_OBRIGATORIO ) );		
		
		try {
			recursoService.registraRecurso( req );
			return ResponseEntity.ok().build();
		} catch (RecursoJaExisteException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.RECURSO_JA_EXISTE ) );
		} 
	}
	
	@PreAuthorize("hasAuthority('recursoWRITE')")
	@PutMapping(value="/atualiza/{recursoId}")
	public ResponseEntity<Object> atualiza( @PathVariable Long recursoId, @RequestBody SaveRecursoRequest req ) {
		if ( req.getNome() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.NOME_OBRIGATORIO ) );
		if ( req.getNome().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.NOME_OBRIGATORIO ) );		
		
		try {
			recursoService.alteraRecurso( recursoId, req );
			return ResponseEntity.ok().build();
		} catch (RecursoJaExisteException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.RECURSO_JA_EXISTE ) );
		} catch (RecursoNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.RECURSO_NAO_ENCONTRADO ) );
		} 
	}
			
	@PreAuthorize("hasAuthority('recursoREAD')")
	@PostMapping(value="/filtra")
	public ResponseEntity<Object> filtra( @RequestBody BuscaRecursosRequest request ) {
		if ( request.getNomeIni() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.NOME_OBRIGATORIO ) );
		if ( request.getNomeIni().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.NOME_OBRIGATORIO ) );		
		
		List<RecursoResponse> lista = recursoService.filtraRecursos( request );
		return ResponseEntity.ok( lista );
	}
	
	@PreAuthorize("hasAuthority('recursoREAD')")
	@GetMapping(value="/get/{recursoId}")
	public ResponseEntity<Object> busca( @PathVariable Long recursoId ) {				
		try {
			RecursoResponse resp = recursoService.buscaRecurso( recursoId );
			return ResponseEntity.ok( resp );
		} catch (RecursoNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.RECURSO_NAO_ENCONTRADO ) );
		}
	}
	
	@PreAuthorize("hasAuthority('recursoDELETE')")
	@DeleteMapping(value="/deleta/{recursoId}")
	public ResponseEntity<Object> deleta( @PathVariable Long recursoId ) {
		try {
			recursoService.deletaRecurso( recursoId );
			return ResponseEntity.ok().build();
		} catch (RecursoNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.RECURSO_NAO_ENCONTRADO ) );
		}
	}
	
	
}
