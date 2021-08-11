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

import italo.siserp.exception.CategoriaNaoEncontradaException;
import italo.siserp.exception.SubCategoriaJaExisteException;
import italo.siserp.exception.SubCategoriaNaoEncontradaException;
import italo.siserp.model.request.BuscaSubCategoriasRequest;
import italo.siserp.model.request.SaveSubCategoriaRequest;
import italo.siserp.model.response.ErroResponse;
import italo.siserp.model.response.IdResponse;
import italo.siserp.model.response.SubCategoriaResponse;
import italo.siserp.service.SubCategoriaService;

@RestController
@RequestMapping(value="/api/subcategoria")
public class SubCategoriaController {
	
	@Autowired
	private SubCategoriaService subcategoriaService;
	
	@PreAuthorize("hasAuthority('subcategoriaWRITE')")	
	@PostMapping("/registra/{categoriaId}")
	public ResponseEntity<Object> registra( @PathVariable Long categoriaId, @RequestBody SaveSubCategoriaRequest request ) {						
		if ( request.getDescricao() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.SUBCATEGORIA_DESCRICAO_OBRIGATORIA ) );		
		if ( request.getDescricao().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.SUBCATEGORIA_DESCRICAO_OBRIGATORIA ) );		
		
		try {
			IdResponse resp = subcategoriaService.registraSubCategoria( categoriaId, request );
			return ResponseEntity.ok( resp );
		} catch (SubCategoriaJaExisteException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.SUBCATEGORIA_JA_EXISTE ) );		
		} catch (CategoriaNaoEncontradaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CATEGORIA_NAO_ENCONTRADA ) );		
		}				
	}
	
	@PreAuthorize("hasAuthority('subcategoriaWRITE')")	
	@PutMapping("/atualiza/{id}")
	public ResponseEntity<Object> atualiza( @PathVariable Long id, @RequestBody SaveSubCategoriaRequest request ) {	
		if ( request.getDescricao() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.SUBCATEGORIA_DESCRICAO_OBRIGATORIA ) );		
		if ( request.getDescricao().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.SUBCATEGORIA_DESCRICAO_OBRIGATORIA ) );		
		
		try {
			subcategoriaService.atualizaSubCategoria( id, request );
			return ResponseEntity.ok().build();
		} catch (SubCategoriaNaoEncontradaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.SUBCATEGORIA_NAO_ENCONTRADA ) );		
		} catch (SubCategoriaJaExisteException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.SUBCATEGORIA_JA_EXISTE ) );		
		}				
	}
	
	@PreAuthorize("hasAuthority('subcategoriaREAD')")	
	@PostMapping("/filtra/{categoriaId}")
	public ResponseEntity<Object> buscaSubCategorias( 
			@PathVariable Long categoriaId, @RequestBody BuscaSubCategoriasRequest request ) {
		
		if ( request.getDescricaoIni() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.SUBCATEGORIA_DESCRICAO_OBRIGATORIA ) );
		if ( request.getDescricaoIni().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.SUBCATEGORIA_DESCRICAO_OBRIGATORIA ) );
		
		Pageable p = Pageable.unpaged();
		
		List<SubCategoriaResponse> subcategorias = subcategoriaService.buscaSubCategoriasPorDescricaoIni( categoriaId, request, p );
		return ResponseEntity.ok( subcategorias );		
	}

	@PreAuthorize("hasAuthority('subcategoriaREAD')")	
	@PostMapping("/filtra/limit/{categoria}/{limit}")
	public ResponseEntity<Object> buscaSubCategorias( 
			@PathVariable String categoria, 
			@PathVariable Integer limit, 
			@RequestBody BuscaSubCategoriasRequest request ) {
		
		if ( request.getDescricaoIni() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.SUBCATEGORIA_DESCRICAO_OBRIGATORIA ) );
		
		Pageable p = PageRequest.of( 0, limit );
		
		List<SubCategoriaResponse> subcategorias = subcategoriaService.buscaSubCategoriasPorDescricaoIni( categoria, request, p );
		return ResponseEntity.ok( subcategorias );		
	}
	
	@PreAuthorize("hasAuthority('subcategoriaREAD')")	
	@GetMapping("/get/{id}")
	public ResponseEntity<Object> buscaSubCategoriaPorId( @PathVariable Long id ) {		
		try {
			SubCategoriaResponse subcategoria = subcategoriaService.buscaSubCategoriaPorId( id );
			return ResponseEntity.ok( subcategoria );
		} catch (SubCategoriaNaoEncontradaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.SUBCATEGORIA_NAO_ENCONTRADA ) );		
		}
	}
	
	@PreAuthorize("hasAuthority('subcategoriaDELETE')")	
	@DeleteMapping("/deleta/{id}")
	public ResponseEntity<Object> deletaSubCategoria( @PathVariable Long id ) {		
		try {
			subcategoriaService.deleta( id );
			return ResponseEntity.ok().build();
		} catch (SubCategoriaNaoEncontradaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.SUBCATEGORIA_NAO_ENCONTRADA ) );		
		}
	}
	
}
