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

import italo.siserp.exception.CategoriaJaExisteException;
import italo.siserp.exception.CategoriaNaoEncontradaException;
import italo.siserp.model.request.BuscaCategoriasRequest;
import italo.siserp.model.request.SaveCategoriaRequest;
import italo.siserp.model.response.CategoriaResponse;
import italo.siserp.model.response.ErroResponse;
import italo.siserp.model.response.IdResponse;
import italo.siserp.service.CategoriaService;

@RestController
@RequestMapping(value="/api/categoria")
public class CategoriaController {
	
	@Autowired
	private CategoriaService categoriaService;
	
	@PreAuthorize("hasAnyAuthority('ADMIN', 'GERENTE')")
	@PostMapping("/registra")
	public ResponseEntity<Object> registra( @RequestBody SaveCategoriaRequest request ) {						
		if ( request.getDescricao() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CATEGORIA_DESCRICAO_OBRIGATORIA ) );		
		if ( request.getDescricao().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CATEGORIA_DESCRICAO_OBRIGATORIA ) );		
		
		try {
			IdResponse resp = categoriaService.registraCategoria( request );
			return ResponseEntity.ok( resp );
		} catch (CategoriaJaExisteException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CATEGORIA_JA_EXISTE ) );		
		}				
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN', 'GERENTE')")
	@PutMapping("/atualiza/{id}")
	public ResponseEntity<Object> atualiza( @PathVariable Long id, @RequestBody SaveCategoriaRequest request ) {	
		if ( request.getDescricao() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CATEGORIA_DESCRICAO_OBRIGATORIA ) );		
		if ( request.getDescricao().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CATEGORIA_DESCRICAO_OBRIGATORIA ) );		
		
		try {
			categoriaService.atualizaCategoria( id, request );
			return ResponseEntity.ok().build();
		} catch (CategoriaNaoEncontradaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CATEGORIA_NAO_ENCONTRADA ) );		
		} catch (CategoriaJaExisteException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CATEGORIA_JA_EXISTE ) );		
		}				
	}

	@PreAuthorize("hasAnyAuthority('ADMIN', 'GERENTE', 'CAIXA')")
	@PostMapping("/filtra")
	public ResponseEntity<Object> buscaCategorias( @RequestBody BuscaCategoriasRequest request ) {		
		if ( request.getDescricaoIni() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CATEGORIA_DESCRICAO_OBRIGATORIA ) );
		if ( request.getDescricaoIni().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CATEGORIA_DESCRICAO_OBRIGATORIA ) );
		
		List<CategoriaResponse> categorias = categoriaService.buscaCategoriasPorDescricaoIni( request );
		return ResponseEntity.ok( categorias );		
	}

	@PreAuthorize("hasAnyAuthority('ADMIN', 'GERENTE', 'CAIXA')")	
	@GetMapping("/get/{id}")
	public ResponseEntity<Object> buscaCategoriaPorId( @PathVariable Long id ) {		
		try {
			CategoriaResponse categoria = categoriaService.buscaCategoriaPorId( id );
			return ResponseEntity.ok( categoria );
		} catch (CategoriaNaoEncontradaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CATEGORIA_NAO_ENCONTRADA ) );		
		}
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN', 'GERENTE')")
	@DeleteMapping("/deleta/{id}")
	public ResponseEntity<Object> deletaCategoria( @PathVariable Long id ) {		
		try {
			categoriaService.deleta( id );
			return ResponseEntity.ok().build();
		} catch (CategoriaNaoEncontradaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CATEGORIA_NAO_ENCONTRADA ) );		
		}
	}
	
}
