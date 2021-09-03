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

import italo.siserp.exception.CategoriaMapJaExisteException;
import italo.siserp.exception.CategoriaMapNaoEncontradaException;
import italo.siserp.model.request.BuscaCategoriaMapsRequest;
import italo.siserp.model.request.SaveCategoriaMapRequest;
import italo.siserp.model.response.CategoriaMapResponse;
import italo.siserp.model.response.ErroResponse;
import italo.siserp.model.response.IdResponse;
import italo.siserp.service.CategoriaMapService;

@RestController
@RequestMapping(value="/api/categoriamap")
public class CategoriaMapController {
	
	@Autowired
	private CategoriaMapService categoriaMapService;
	
	@PreAuthorize("hasAuthority('categoriaWRITE')")
	@PostMapping("/registra")
	public ResponseEntity<Object> registra( @RequestBody SaveCategoriaMapRequest request ) {						
		if ( request.getCategoria() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CATEGORIA_DESCRICAO_OBRIGATORIA ) );		
		if ( request.getCategoria().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CATEGORIA_DESCRICAO_OBRIGATORIA ) );		
		if ( request.getSubcategoria() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.SUBCATEGORIA_DESCRICAO_OBRIGATORIA ) );		
		if ( request.getSubcategoria().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.SUBCATEGORIA_DESCRICAO_OBRIGATORIA ) );		
		
		try {
			IdResponse resp = categoriaMapService.registraCategoriaMap( request );
			return ResponseEntity.ok( resp );
		} catch (CategoriaMapJaExisteException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CATEGORIA_MAP_JA_EXISTE ) );		
		}				
	}
	
	@PreAuthorize("hasAuthority('categoriaWRITE')")
	@PutMapping("/atualiza/{id}")
	public ResponseEntity<Object> atualiza( @PathVariable Long id, @RequestBody SaveCategoriaMapRequest request ) {	
		if ( request.getCategoria() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CATEGORIA_DESCRICAO_OBRIGATORIA ) );		
		if ( request.getCategoria().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CATEGORIA_DESCRICAO_OBRIGATORIA ) );		
		if ( request.getSubcategoria() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.SUBCATEGORIA_DESCRICAO_OBRIGATORIA ) );		
		if ( request.getSubcategoria().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.SUBCATEGORIA_DESCRICAO_OBRIGATORIA ) );		
		
		try {
			categoriaMapService.atualizaCategoriaMap( id, request );
			return ResponseEntity.ok().build();
		} catch (CategoriaMapNaoEncontradaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CATEGORIA_MAP_NAO_ENCONTRADA ) );		
		} catch (CategoriaMapJaExisteException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CATEGORIA_MAP_JA_EXISTE ) );		
		}				
	}

	@PreAuthorize("hasAuthority('categoriaREAD')")
	@PostMapping("/filtra")
	public ResponseEntity<Object> buscaCategorias( @RequestBody BuscaCategoriaMapsRequest request ) {		
		if ( request.getCategoriaIni() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CATEGORIA_DESCRICAO_OBRIGATORIA ) );
		if ( request.getCategoriaIni().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CATEGORIA_DESCRICAO_OBRIGATORIA ) );
		if ( request.getSubcategoriaIni() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.SUBCATEGORIA_DESCRICAO_OBRIGATORIA ) );
		if ( request.getSubcategoriaIni().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.SUBCATEGORIA_DESCRICAO_OBRIGATORIA ) );
		
		Pageable p = Pageable.unpaged();
		
		List<CategoriaMapResponse> categorias = categoriaMapService.filtra( request, p );
		return ResponseEntity.ok( categorias );		
	}
				
	@PreAuthorize("hasAuthority('categoriaREAD')")
	@GetMapping("/filtra/categoria/limit/{categoriaIni}/{limit}")
	public ResponseEntity<Object> buscaCategorias( @PathVariable String categoriaIni, @PathVariable Integer limit ) {				
		Pageable p = PageRequest.of( 0, limit );
				
		List<CategoriaMapResponse> maps = categoriaMapService.filtraCategorias( categoriaIni, p );
		return ResponseEntity.ok( maps );		
	}
	
	@PreAuthorize("hasAuthority('categoriaREAD')")
	@GetMapping("/filtra/subcategoria/limit/{categoria}/{subcategoriaIni}/{limit}")
	public ResponseEntity<Object> buscaSubcategorias( @PathVariable String categoria, @PathVariable String subcategoriaIni, @PathVariable Integer limit ) {				
		Pageable p = PageRequest.of( 0, limit );
				
		List<CategoriaMapResponse> maps = categoriaMapService.filtraSubcategorias( categoria, subcategoriaIni, p );
		return ResponseEntity.ok( maps );		
	}
			
	@PreAuthorize("hasAuthority('categoriaREAD')")
	@PostMapping("/filtra/limit/{limit}")
	public ResponseEntity<Object> buscaCategorias( @PathVariable Integer limit, BuscaCategoriaMapsRequest request ) {				
		Pageable p = PageRequest.of( 0, limit );
				
		List<CategoriaMapResponse> categorias = categoriaMapService.filtra( request, p );
		return ResponseEntity.ok( categorias );		
	}

	@PreAuthorize("hasAuthority('categoriaREAD')")
	@GetMapping("/get/{id}")
	public ResponseEntity<Object> buscaCategoriaPorId( @PathVariable Long id ) {		
		try {
			CategoriaMapResponse categoria = categoriaMapService.buscaCategoriaMapPorId( id );
			return ResponseEntity.ok( categoria );
		} catch (CategoriaMapNaoEncontradaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CATEGORIA_MAP_NAO_ENCONTRADA ) );		
		}
	}
	
	@PreAuthorize("hasAuthority('categoriaDELETE')")
	@DeleteMapping("/deleta/{id}")
	public ResponseEntity<Object> deletaCategoria( @PathVariable Long id ) {		
		try {
			categoriaMapService.deleta( id );
			return ResponseEntity.ok().build();
		} catch (CategoriaMapNaoEncontradaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CATEGORIA_MAP_NAO_ENCONTRADA ) );		
		}
	}
	
}
