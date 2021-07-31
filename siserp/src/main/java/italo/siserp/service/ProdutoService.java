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
import italo.siserp.model.Produto;
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
	
	public List<ProdutoResponse> buscaProdutosPorDescIni( String descricaoIni ) {
		String dini = (descricaoIni.equals( "*" ) ? "" : descricaoIni );
		
		List<Produto> produtos = produtoRepository.filtraPorDescIni( dini+"%" );
		
		List<ProdutoResponse> responses = new ArrayList<>();
		
		for( Produto p : produtos ) {			
			ProdutoResponse resp = produtoBuilder.novoProdutoResponse();
			produtoBuilder.carregaProdutoResponse( resp, p );
			
			responses.add( resp );
		}
		
		return responses;
	}
	
	public ProdutoResponse buscaProdutoPorCodBarra( String codigoBarras ) throws ProdutoNaoEncontradoException {		
		Produto p = produtoRepository.findByCodigoBarras( codigoBarras ).orElseThrow( ProdutoNaoEncontradoException::new ); 
		
		ProdutoResponse resp = produtoBuilder.novoProdutoResponse();
		produtoBuilder.carregaProdutoResponse( resp, p );		
		return resp;
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