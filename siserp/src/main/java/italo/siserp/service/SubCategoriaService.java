package italo.siserp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import italo.siserp.builder.SubCategoriaBuilder;
import italo.siserp.exception.CategoriaNaoEncontradaException;
import italo.siserp.exception.SubCategoriaJaExisteException;
import italo.siserp.exception.SubCategoriaNaoEncontradaException;
import italo.siserp.model.Categoria;
import italo.siserp.model.SubCategoria;
import italo.siserp.repository.CategoriaRepository;
import italo.siserp.repository.SubCategoriaRepository;
import italo.siserp.service.request.BuscaSubCategoriasRequest;
import italo.siserp.service.request.SaveSubCategoriaRequest;
import italo.siserp.service.response.IdResponse;
import italo.siserp.service.response.SubCategoriaResponse;

@Service
public class SubCategoriaService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private SubCategoriaRepository subcategoriaRepository;
	
	@Autowired
	private SubCategoriaBuilder subcategoriaBuilder;
		
	public IdResponse registraSubCategoria( Long categoriaId, SaveSubCategoriaRequest request ) 
			throws SubCategoriaJaExisteException, CategoriaNaoEncontradaException {				

		String descricao = request.getDescricao();
		Categoria cat =	categoriaRepository.findById( categoriaId ).orElseThrow( CategoriaNaoEncontradaException::new ); 				
		
		List<SubCategoria> subcats = cat.getSubcategorias();
		for( SubCategoria sc : subcats )
			if ( sc.getDescricao().equalsIgnoreCase( descricao ) )		
				throw new SubCategoriaJaExisteException();
						
		SubCategoria sc = subcategoriaBuilder.novoSubCategoria();
		subcategoriaBuilder.carregaSubCategoria( sc, request );
		
		sc.setCategoria( cat ); 
		
		subcategoriaRepository.save( sc );
					
		return new IdResponse( sc.getId() ); 
	}
	
	public void atualizaSubCategoria( Long id, SaveSubCategoriaRequest req )
			throws SubCategoriaNaoEncontradaException, 
					SubCategoriaJaExisteException {
		
		SubCategoria sc = subcategoriaRepository.findById( id ).orElseThrow( SubCategoriaNaoEncontradaException::new );
		Categoria c = sc.getCategoria();
		
		String descricao = req.getDescricao();
		
		if ( !descricao.equalsIgnoreCase( sc.getDescricao() ) ) {
			List<SubCategoria> subcats = c.getSubcategorias();
			for( SubCategoria sc2 : subcats )
				if ( sc2.getDescricao().equalsIgnoreCase( descricao ) )
					throw new SubCategoriaJaExisteException();
		}
				
		subcategoriaBuilder.carregaSubCategoria( sc, req );
		
		subcategoriaRepository.save( sc );
	}
		
	public List<SubCategoriaResponse> buscaSubCategoriasPorDescricaoIni( String categoriaDesc, BuscaSubCategoriasRequest request, Pageable p ) {
		String descricaoIni = ( request.getDescricaoIni().equals( "*" ) ? "" : request.getDescricaoIni() );
		
		List<SubCategoria> subcategorias = subcategoriaRepository.filtra( categoriaDesc, descricaoIni+"%", p );
		
		List<SubCategoriaResponse> responses = new ArrayList<>();
		
		for( SubCategoria sc : subcategorias ) {			
			SubCategoriaResponse resp = subcategoriaBuilder.novoSubCategoriaResponse();
			subcategoriaBuilder.carregaSubCategoriaResponse( resp, sc );
			
			responses.add( resp );
		}
		
		return responses;
	}
	
	public List<SubCategoriaResponse> buscaSubCategoriasPorDescricaoIni( Long categoriaId, BuscaSubCategoriasRequest request, Pageable p ) {
		String descricaoIni = ( request.getDescricaoIni().equals( "*" ) ? "" : request.getDescricaoIni() );
		
		List<SubCategoria> subcategorias = subcategoriaRepository.filtra( categoriaId, descricaoIni+"%", p );
		
		List<SubCategoriaResponse> responses = new ArrayList<>();
		
		for( SubCategoria sc : subcategorias ) {			
			SubCategoriaResponse resp = subcategoriaBuilder.novoSubCategoriaResponse();
			subcategoriaBuilder.carregaSubCategoriaResponse( resp, sc );
			
			responses.add( resp );
		}
		
		return responses;
	}
	
	public SubCategoriaResponse buscaSubCategoriaPorId( Long id ) throws SubCategoriaNaoEncontradaException {
		SubCategoria sc = subcategoriaRepository.findById( id ).orElseThrow( SubCategoriaNaoEncontradaException::new );
				
		SubCategoriaResponse resp = subcategoriaBuilder.novoSubCategoriaResponse();
		subcategoriaBuilder.carregaSubCategoriaResponse( resp, sc ); 
		
		return resp;
	}
		
	public void deleta( Long id ) throws SubCategoriaNaoEncontradaException {
		if ( !subcategoriaRepository.existsById( id ) )
			throw new SubCategoriaNaoEncontradaException();
		
		subcategoriaRepository.deleteById( id ); 
	}
		
}

