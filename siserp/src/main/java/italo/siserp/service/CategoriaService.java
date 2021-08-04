package italo.siserp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import italo.siserp.builder.CategoriaBuilder;
import italo.siserp.exception.CategoriaJaExisteException;
import italo.siserp.exception.CategoriaNaoEncontradaException;
import italo.siserp.model.Categoria;
import italo.siserp.model.request.BuscaCategoriasRequest;
import italo.siserp.model.request.SaveCategoriaRequest;
import italo.siserp.model.response.CategoriaResponse;
import italo.siserp.model.response.IdResponse;
import italo.siserp.repository.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private CategoriaBuilder categoriaBuilder;
		
	public IdResponse registraCategoria( SaveCategoriaRequest request ) throws CategoriaJaExisteException {				
		if ( this.existeCategoriaPorDescricao( request.getDescricao() ) )
			throw new CategoriaJaExisteException();
				
		Categoria c = categoriaBuilder.novoCategoria();
		categoriaBuilder.carregaCategoria( c, request ); 
		
		categoriaRepository.save( c );
					
		return new IdResponse( c.getId() ); 
	}
	
	public void atualizaCategoria( Long id, SaveCategoriaRequest req )
			throws CategoriaNaoEncontradaException, 
					CategoriaJaExisteException {
		
		Categoria c = categoriaRepository.findById( id ).orElseThrow( CategoriaNaoEncontradaException::new );
		
		String descricao = req.getDescricao();
		
		if ( !descricao.equalsIgnoreCase( c.getDescricao() ) )
			if ( this.existeCategoriaPorDescricao( descricao ) )
				throw new CategoriaJaExisteException();
				
		categoriaBuilder.carregaCategoria( c, req );
		
		categoriaRepository.save( c );
	}
		
	public List<CategoriaResponse> buscaCategoriasPorDescricaoIni( BuscaCategoriasRequest request, Pageable p ) {
		String descricaoIni = ( request.getDescricaoIni().equals( "*" ) ? "" : request.getDescricaoIni() );
		
		List<Categoria> categorias = categoriaRepository.filtra( descricaoIni+"%", p );
		
		List<CategoriaResponse> responses = new ArrayList<>();
		
		for( Categoria c : categorias ) {			
			CategoriaResponse resp = categoriaBuilder.novoCategoriaResponse();
			categoriaBuilder.carregaCategoriaResponse( resp, c );
			
			responses.add( resp );
		}
		
		return responses;
	}
	
	public CategoriaResponse buscaCategoriaPorId( Long id ) throws CategoriaNaoEncontradaException {
		Categoria c = categoriaRepository.findById( id ).orElseThrow( CategoriaNaoEncontradaException::new );
				
		CategoriaResponse resp = categoriaBuilder.novoCategoriaResponse();
		categoriaBuilder.carregaCategoriaResponse( resp, c ); 
		
		return resp;
	}
	
	public boolean existeCategoriaPorDescricao( String descricao ) {
		Categoria c = new Categoria();
		c.setDescricao( descricao );
		
		ExampleMatcher em = ExampleMatcher.matching()
				.withMatcher( "descricao", ExampleMatcher.GenericPropertyMatchers.ignoreCase() );
		
		Example<Categoria> ex = Example.of( c, em );
		
		return categoriaRepository.exists( ex );
	}
	
	public void deleta( Long id ) throws CategoriaNaoEncontradaException {
		if ( !categoriaRepository.existsById( id ) )
			throw new CategoriaNaoEncontradaException();
		
		categoriaRepository.deleteById( id ); 
	}
		
}
