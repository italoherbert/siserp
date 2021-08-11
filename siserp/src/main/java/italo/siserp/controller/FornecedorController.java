package italo.siserp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import italo.siserp.exception.FornecedorJaExisteException;
import italo.siserp.exception.FornecedorNaoEncontradoException;
import italo.siserp.model.request.BuscaFornecedoresRequest;
import italo.siserp.model.request.SaveFornecedorRequest;
import italo.siserp.model.response.ErroResponse;
import italo.siserp.model.response.FornecedorResponse;
import italo.siserp.model.response.IdResponse;
import italo.siserp.service.FornecedorService;

@RestController
@RequestMapping(value="/api/fornecedor")
public class FornecedorController {
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@PreAuthorize("hasAuthority('fornecedorWRITE')")	
	@PostMapping("/registra")
	public ResponseEntity<Object> registra( @RequestBody SaveFornecedorRequest request ) {						
		if ( request.getEmpresa() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FORNECEDOR_EMPRESA_OBRIGATORIA ) );		
		if ( request.getEmpresa().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FORNECEDOR_EMPRESA_OBRIGATORIA ) );		
		
		try {
			IdResponse resp = fornecedorService.registraFornecedor( request );
			return ResponseEntity.ok( resp );
		} catch (FornecedorJaExisteException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FORNECEDOR_JA_EXISTE ) );		
		}				
	}
	
	@PreAuthorize("hasAuthority('fornecedorWRITE')")	
	@PutMapping("/atualiza/{id}")
	public ResponseEntity<Object> atualiza( @PathVariable Long id, @RequestBody SaveFornecedorRequest request ) {	
		if ( request.getEmpresa() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FORNECEDOR_EMPRESA_OBRIGATORIA ) );		
		if ( request.getEmpresa().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FORNECEDOR_EMPRESA_OBRIGATORIA ) );		
		
		try {
			fornecedorService.atualizaFornecedor( id, request );
			return ResponseEntity.ok().build();
		} catch (FornecedorNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FORNECEDOR_NAO_ENCONTRADO ) );		
		} catch (FornecedorJaExisteException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FORNECEDOR_JA_EXISTE ) );		
		}				
	}

	@PreAuthorize("hasAuthority('fornecedorREAD')")	
	@PostMapping("/filtra")
	public ResponseEntity<Object> buscaFornecedores( @RequestBody BuscaFornecedoresRequest request ) {		
		if ( request.getEmpresaIni() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FORNECEDOR_EMPRESA_OBRIGATORIA ) );
		if ( request.getEmpresaIni().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FORNECEDOR_EMPRESA_OBRIGATORIA ) );
		
		Pageable p = Pageable.unpaged();
		
		List<FornecedorResponse> fornecedors = fornecedorService.filtra( request, p );
		return ResponseEntity.ok( fornecedors );		
	}
	
	@PreAuthorize("hasAuthority('fornecedorREAD')")	
	@PostMapping("/filtra/limit/{limit}")
	public ResponseEntity<Object> buscaFornecedores( @PathVariable int limit, @RequestBody BuscaFornecedoresRequest request ) {		
		if ( request.getEmpresaIni() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FORNECEDOR_EMPRESA_OBRIGATORIA ) );
		
		Pageable p = PageRequest.of( 0, limit ); 
		
		List<FornecedorResponse> fornecedors = fornecedorService.filtra( request, p );
		return ResponseEntity.ok( fornecedors );		
	}

	@PreAuthorize("hasAuthority('fornecedorREAD')")	
	@GetMapping("/get/{id}")
	public ResponseEntity<Object> buscaFornecedorPorId( @PathVariable Long id ) {		
		try {
			FornecedorResponse fornecedor = fornecedorService.buscaFornecedorPorId( id );
			return ResponseEntity.ok( fornecedor );
		} catch (FornecedorNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FORNECEDOR_NAO_ENCONTRADO ) );		
		}
	}
	
	@PreAuthorize("hasAuthority('fornecedorDELETE')")	
	@DeleteMapping("/deleta/{id}")
	public ResponseEntity<Object> deletaFornecedor( @PathVariable Long id ) {		
		try {
			fornecedorService.deleta( id );
			return ResponseEntity.ok().build();
		} catch (FornecedorNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FORNECEDOR_NAO_ENCONTRADO ) );		
		}
	}
	
}

