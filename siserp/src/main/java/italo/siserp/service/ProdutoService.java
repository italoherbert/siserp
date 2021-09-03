package italo.siserp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.siserp.builder.ProdutoBuilder;
import italo.siserp.exception.PrecoUnitCompraInvalidoException;
import italo.siserp.exception.PrecoUnitVendaInvalidoException;
import italo.siserp.exception.ProdutoJaExisteException;
import italo.siserp.exception.ProdutoNaoEncontradoException;
import italo.siserp.exception.QuantidadeInvalidaException;
import italo.siserp.model.CategoriaMap;
import italo.siserp.model.Produto;
import italo.siserp.model.request.BuscaProdutosRequest;
import italo.siserp.model.request.SaveProdutoRequest;
import italo.siserp.model.response.ProdutoResponse;
import italo.siserp.repository.ProdutoRepository;

@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository produtoRepository;
				
	@Autowired
	private ProdutoBuilder produtoBuilder;		
				
	public void salvaProduto( SaveProdutoRequest req )
			throws ProdutoJaExisteException,					
					PrecoUnitCompraInvalidoException, 
					PrecoUnitVendaInvalidoException, 
					QuantidadeInvalidaException {
				
		String codigoBarras = req.getCodigoBarras();
		Optional<Produto> pop = produtoRepository.findByCodigoBarras( codigoBarras );
		
		Produto p;
		if ( pop.isPresent() ) {
			p = pop.get();
		} else {
			p = produtoBuilder.novoProduto();
		}
								
		produtoBuilder.carregaProduto( p, req );
		
		produtoRepository.save( p );		
	}		
	
	public List<ProdutoResponse> filtra( BuscaProdutosRequest request ) {
		String dini = (request.getDescricaoIni().equals( "*" ) ? "" : request.getDescricaoIni() );
		String catsSubcats = request.getCatsSubcats();
		
		String[] catsSubcatsLista = null;
		if ( catsSubcats != null )
			if ( !catsSubcats.isBlank() )
				catsSubcatsLista = catsSubcats.split( "\\b" );
		
		List<Produto> itens = produtoRepository.filtraPorDescIni( dini+"%" );
		
		List<ProdutoResponse> responses = new ArrayList<>();
		
		for( Produto p : itens ) {
			boolean achou = true;
			if ( catsSubcatsLista != null ) {
				achou = false;
				
				List<CategoriaMap> maps = p.getCategoriaMaps();
				int size = maps.size();
				for( int i = 0; !achou && i < size; i++ ) {
					CategoriaMap map = maps.get( i );
					String catdesc = map.getCategoria();
					String subcatdesc = map.getSubcategoria();
					
					for( int j = 0; !achou && j < catsSubcatsLista.length; j++ ) {
						if ( catsSubcatsLista[ j ].equalsIgnoreCase( catdesc ) || 
								catsSubcatsLista[ j ].equalsIgnoreCase( subcatdesc ) ) {
							achou = true;
						}
					}
				}								
			}
			
			if ( !achou )
				continue;
			
			ProdutoResponse resp = produtoBuilder.novoProdutoResponse();
			produtoBuilder.carregaProdutoResponse( resp, p );
			
			responses.add( resp );
		}
		
		return responses;
	}
	
	public ProdutoResponse buscaProdutoPorCodBarra( String codigoBarras ) throws ProdutoNaoEncontradoException {		
		Produto ip = produtoRepository.findByCodigoBarras( codigoBarras ).orElseThrow( ProdutoNaoEncontradoException::new ); 
		
		ProdutoResponse resp = produtoBuilder.novoProdutoResponse();
		produtoBuilder.carregaProdutoResponse( resp, ip );		
		return resp;
	}
	
	public void verificaSeExisteCodBarra( String codigoBarras ) throws ProdutoNaoEncontradoException {		
		produtoRepository.findByCodigoBarras( codigoBarras ).orElseThrow( ProdutoNaoEncontradoException:: new );		
	}
	
	public ProdutoResponse buscaProdutoPorId( Long id ) throws ProdutoNaoEncontradoException {
		Produto p = produtoRepository.findById( id ).orElseThrow( ProdutoNaoEncontradoException::new );
				
		ProdutoResponse resp = produtoBuilder.novoProdutoResponse();
		produtoBuilder.carregaProdutoResponse( resp, p ); 
		
		return resp;
	}
		
	public void deleta( Long id ) throws ProdutoNaoEncontradoException {
		if ( !produtoRepository.existsById( id ) )
			throw new ProdutoNaoEncontradoException();
		
		produtoRepository.deleteById( id ); 
	}
		
}
