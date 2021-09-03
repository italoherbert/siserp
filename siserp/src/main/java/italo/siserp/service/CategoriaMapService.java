package italo.siserp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import italo.siserp.builder.CategoriaMapBuilder;
import italo.siserp.exception.CategoriaMapJaExisteException;
import italo.siserp.exception.CategoriaMapNaoEncontradaException;
import italo.siserp.model.CategoriaMap;
import italo.siserp.repository.CategoriaMapRepository;
import italo.siserp.service.request.BuscaCategoriaMapsRequest;
import italo.siserp.service.request.SaveCategoriaMapRequest;
import italo.siserp.service.response.CategoriaMapResponse;
import italo.siserp.service.response.IdResponse;

@Service
public class CategoriaMapService {
	
	@Autowired
	private CategoriaMapRepository categoriaMapRepository;
	
	@Autowired
	private CategoriaMapBuilder categoriaMapBuilder;
		
	public IdResponse registraCategoriaMap( SaveCategoriaMapRequest request ) throws CategoriaMapJaExisteException {
		Optional<CategoriaMap> mapOp = categoriaMapRepository.get( request.getCategoria(), request.getSubcategoria() );
		if ( mapOp.isPresent() )
			throw new CategoriaMapJaExisteException();
				
		CategoriaMap c = categoriaMapBuilder.novoCategoriaMap();
		categoriaMapBuilder.carregaCategoriaMap( c, request ); 
		
		categoriaMapRepository.save( c );
					
		return new IdResponse( c.getId() ); 
	}
	
	public void atualizaCategoriaMap( Long id, SaveCategoriaMapRequest req )
			throws CategoriaMapNaoEncontradaException, 
					CategoriaMapJaExisteException {
		
		CategoriaMap map = categoriaMapRepository.findById( id ).orElseThrow( CategoriaMapNaoEncontradaException::new );
		
		String cat = req.getCategoria();
		String subcat = req.getSubcategoria();
		
		if ( !cat.equalsIgnoreCase( map.getCategoria() ) || !subcat.equalsIgnoreCase( map.getSubcategoria() ) ) {
			Optional<CategoriaMap> map2 = categoriaMapRepository.get( cat, subcat );
			if ( map2.isPresent() )
				throw new CategoriaMapJaExisteException();
		}
				
		categoriaMapBuilder.carregaCategoriaMap( map, req );
		
		categoriaMapRepository.save( map );
	}
		
	public List<CategoriaMapResponse> filtra( BuscaCategoriaMapsRequest request, Pageable p ) {
		String catIni = ( request.getCategoriaIni().equals( "*" ) ? "" : request.getCategoriaIni() );
		String subcatIni = (request.getSubcategoriaIni().equals( "*") ? "" : request.getSubcategoriaIni() );
				
		List<CategoriaMap> maps = categoriaMapRepository.filtra( catIni+"%", subcatIni+"%", p );
		
		List<CategoriaMapResponse> responses = new ArrayList<>();
		
		for( CategoriaMap map : maps ) {			
			CategoriaMapResponse resp = categoriaMapBuilder.novoCategoriaMapResponse();
			categoriaMapBuilder.carregaCategoriaMapResponse( resp, map );
			
			responses.add( resp );
		}
		
		return responses;
	}
	
	public List<CategoriaMapResponse> filtraCategorias( String categoriaIni, Pageable p ) {				
		String catIni = categoriaIni.equals( "*" ) ? "" : categoriaIni;
		List<CategoriaMap> maps = categoriaMapRepository.filtraCategorias( catIni+"%", p );
		
		List<CategoriaMapResponse> responses = new ArrayList<>();
		
		for( CategoriaMap map : maps ) {			
			CategoriaMapResponse resp = categoriaMapBuilder.novoCategoriaMapResponse();
			categoriaMapBuilder.carregaCategoriaMapResponse( resp, map );
			
			responses.add( resp );
		}
		
		return responses;
	}
	
	public List<CategoriaMapResponse> filtraSubcategorias( String categoria, String subcategoriaIni, Pageable p ) {
		String subcatIni = subcategoriaIni.equals( "*" ) ? "" : subcategoriaIni;
		
		List<CategoriaMap> maps = categoriaMapRepository.filtraSubcategorias( categoria, subcatIni+"%", p );
		
		List<CategoriaMapResponse> responses = new ArrayList<>();
		
		for( CategoriaMap map : maps ) {			
			CategoriaMapResponse resp = categoriaMapBuilder.novoCategoriaMapResponse();
			categoriaMapBuilder.carregaCategoriaMapResponse( resp, map );
			
			responses.add( resp );
		}
		
		return responses;
	}
	
	public CategoriaMapResponse buscaCategoriaMapPorId( Long id ) throws CategoriaMapNaoEncontradaException {
		CategoriaMap c = categoriaMapRepository.findById( id ).orElseThrow( CategoriaMapNaoEncontradaException::new );
				
		CategoriaMapResponse resp = categoriaMapBuilder.novoCategoriaMapResponse();
		categoriaMapBuilder.carregaCategoriaMapResponse( resp, c ); 
		
		return resp;
	}
		
	public void deleta( Long id ) throws CategoriaMapNaoEncontradaException {
		if ( !categoriaMapRepository.existsById( id ) )
			throw new CategoriaMapNaoEncontradaException();
		
		categoriaMapRepository.deleteById( id ); 
	}
		
}
