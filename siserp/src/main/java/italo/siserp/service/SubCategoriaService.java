package italo.siserp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.siserp.component.builder.SubCategoriaBuilder;
import italo.siserp.exception.CategoriaNaoEncontradaException;
import italo.siserp.exception.SubCategoriaJaExisteException;
import italo.siserp.exception.SubCategoriaNaoEncontradaException;
import italo.siserp.model.Categoria;
import italo.siserp.model.SubCategoria;
import italo.siserp.model.request.BuscaSubCategoriasRequest;
import italo.siserp.model.request.SaveSubCategoriaRequest;
import italo.siserp.model.response.IdResponse;
import italo.siserp.model.response.SubCategoriaResponse;
import italo.siserp.repository.CategoriaRepository;
import italo.siserp.repository.SubCategoriaRepository;

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
		if ( subcategoriaRepository.existePorDescricao( categoriaId, descricao ) )
			throw new SubCategoriaJaExisteException();
				
		boolean existeCategoria = categoriaRepository.existsById( categoriaId );
		if ( !existeCategoria )
			throw new CategoriaNaoEncontradaException();
		
		SubCategoria sc = subcategoriaBuilder.novoSubCategoria( categoriaId );
		subcategoriaBuilder.carregaSubCategoria( sc, request ); 
		
		subcategoriaRepository.save( sc );
					
		return new IdResponse( sc.getId() ); 
	}
	
	public void atualizaSubCategoria( Long id, SaveSubCategoriaRequest req )
			throws SubCategoriaNaoEncontradaException, 
					SubCategoriaJaExisteException {
		
		SubCategoria sc = subcategoriaRepository.findById( id ).orElseThrow( SubCategoriaNaoEncontradaException::new );
		Categoria c = sc.getCategoria();
		
		String descricao = req.getDescricao();
		
		if ( !descricao.equalsIgnoreCase( sc.getDescricao() ) )
			if ( subcategoriaRepository.existePorDescricao( c.getId(), descricao ) )
				throw new SubCategoriaJaExisteException();
				
		subcategoriaBuilder.carregaSubCategoria( sc, req );
		
		subcategoriaRepository.save( sc );
	}
		
	public List<SubCategoriaResponse> buscaSubCategoriasPorDescricaoIni( Long categoriaId, BuscaSubCategoriasRequest request ) {
		String descricaoIni = ( request.getDescricaoIni().equals( "*" ) ? "" : request.getDescricaoIni() );
		
		List<SubCategoria> subcategorias = subcategoriaRepository.filtra( categoriaId, descricaoIni+"%" );
		
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

