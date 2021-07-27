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

import italo.siserp.exception.EmpresaJaExisteException;
import italo.siserp.exception.EmpresaNaoEncontradaException;
import italo.siserp.model.request.BuscaEmpresasRequest;
import italo.siserp.model.request.SaveEmpresaRequest;
import italo.siserp.model.response.EmpresaResponse;
import italo.siserp.model.response.ErroResponse;
import italo.siserp.model.response.IdResponse;
import italo.siserp.service.EmpresaService;

@RestController
@RequestMapping(value="/api/empresa")
public class EmpresaController {
	
	@Autowired
	private EmpresaService empresaService;
			
	@PreAuthorize("hasAuthority('RAIZ')")
	@PostMapping(value="/registra")
	public ResponseEntity<Object> registra( @RequestBody SaveEmpresaRequest request ) {
		if ( request.getRazaoSocial() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.RAZAO_SOCIAL_OBRIGATORIA ) );
		if ( request.getRazaoSocial().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.RAZAO_SOCIAL_OBRIGATORIA ) );
		
		try {
			IdResponse resp = empresaService.registraEmpresa( request );
			return ResponseEntity.ok( resp );
		} catch (EmpresaJaExisteException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.EMPRESA_JA_EXISTE ) );
		}
	}
	
	@PreAuthorize("hasAnyAuthority('RAIZ', 'ADMIN')")
	@PutMapping(value="/atualiza/{id}")
	public ResponseEntity<Object> atualiza( @PathVariable Long id, @RequestBody SaveEmpresaRequest request ) {				
	
		if ( request.getRazaoSocial() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.RAZAO_SOCIAL_OBRIGATORIA ) );
		if ( request.getRazaoSocial().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.RAZAO_SOCIAL_OBRIGATORIA ) );
		
		try {
			empresaService.atualizaEmpresa( id, request );
			return ResponseEntity.ok().build();
		} catch (EmpresaNaoEncontradaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.EMPRESA_NAO_ENCONTRADA ) );
		} catch (EmpresaJaExisteException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.EMPRESA_JA_EXISTE ) );
		}
	}
	
	@PreAuthorize("hasAnyAuthority('RAIZ')")
	@PostMapping(value="/filtra")
	public ResponseEntity<Object> filtra( @RequestBody BuscaEmpresasRequest request ) {				
		if ( request.getRazaoSocialIni() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.RAZAO_SOCIAL_OBRIGATORIA ) );
		if ( request.getRazaoSocialIni().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.RAZAO_SOCIAL_OBRIGATORIA ) );
		
		
		List<EmpresaResponse> resps = empresaService.filtraEmpresas( request );
		return ResponseEntity.ok( resps );	
	}
	
	@PreAuthorize("hasAnyAuthority('RAIZ', 'ADMIN', 'GERENTE', 'CAIXA')")	
	@GetMapping(value="/get/{id}")
	public ResponseEntity<Object> get( @PathVariable Long id ) {		
		try {
			EmpresaResponse resp = empresaService.getEmpresa( id );
			return ResponseEntity.ok( resp );	
		} catch (EmpresaNaoEncontradaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.EMPRESA_NAO_ENCONTRADA ) );
		}
	}

	@PreAuthorize("hasAuthority('RAIZ')")	
	@DeleteMapping(value="/deleta/{id}")
	public ResponseEntity<Object> deleta( @PathVariable Long id ) {		
		try {
			empresaService.deletaEmpresa( id );
			return ResponseEntity.ok().build();	
		} catch (EmpresaNaoEncontradaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.EMPRESA_NAO_ENCONTRADA ) );
		}
	}
	
}
