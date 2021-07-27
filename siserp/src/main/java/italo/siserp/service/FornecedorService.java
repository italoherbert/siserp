package italo.siserp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import italo.siserp.component.builder.FornecedorBuilder;
import italo.siserp.exception.FornecedorJaExisteException;
import italo.siserp.exception.FornecedorNaoEncontradoException;
import italo.siserp.model.Fornecedor;
import italo.siserp.model.request.BuscaFornecedoresRequest;
import italo.siserp.model.request.SaveFornecedorRequest;
import italo.siserp.model.response.FornecedorResponse;
import italo.siserp.model.response.IdResponse;
import italo.siserp.repository.FornecedorRepository;

@Service
public class FornecedorService {
	@Autowired
	private FornecedorRepository fornecedorRepository;
				
	@Autowired
	private FornecedorBuilder fornecedorBuilder;
	
	public IdResponse registraFornecedor( SaveFornecedorRequest request ) throws FornecedorJaExisteException {				
		if ( this.existeFornecedorPorNomeEmpresa( request.getEmpresa() ) )
			throw new FornecedorJaExisteException();
				
		Fornecedor f = fornecedorBuilder.novoFornecedor();
		fornecedorBuilder.carregaFornecedor( f, request ); 
		
		fornecedorRepository.save( f );
					
		return new IdResponse( f.getId() ); 
	}
	
	public void atualizaFornecedor( Long id, SaveFornecedorRequest req )
			throws FornecedorNaoEncontradoException, 
					FornecedorJaExisteException {
		
		Fornecedor f = fornecedorRepository.findById( id ).orElseThrow( FornecedorNaoEncontradoException::new );
		
		String empresa = req.getEmpresa();
		
		if ( !empresa.equalsIgnoreCase( f.getEmpresa() ) )
			if ( this.existeFornecedorPorNomeEmpresa( empresa ) )
				throw new FornecedorJaExisteException();
				
		fornecedorBuilder.carregaFornecedor( f, req );
		
		fornecedorRepository.save( f );
	}
	
	public List<FornecedorResponse> buscaFornecedorsPorEmpresaIni( BuscaFornecedoresRequest request ) {
		String empresaIni = (request.getEmpresaIni().equals( "*" ) ? "" : request.getEmpresaIni() );
		
		List<Fornecedor> fornecedors = fornecedorRepository.filtra( empresaIni+"%" );
		
		List<FornecedorResponse> responses = new ArrayList<>();
		
		for( Fornecedor f : fornecedors ) {			
			FornecedorResponse resp = fornecedorBuilder.novoFornecedorResponse();
			fornecedorBuilder.carregaFornecedorResponse( resp, f );
			
			responses.add( resp );
		}
		
		return responses;
	}
	
	public FornecedorResponse buscaFornecedorPorId( Long id ) throws FornecedorNaoEncontradoException {
		Fornecedor f = fornecedorRepository.findById( id ).orElseThrow( FornecedorNaoEncontradoException::new );
				
		FornecedorResponse resp = fornecedorBuilder.novoFornecedorResponse();
		fornecedorBuilder.carregaFornecedorResponse( resp, f ); 
		
		return resp;
	}
	
	public boolean existeFornecedorPorNomeEmpresa( String empresa ) {
		Fornecedor f = new Fornecedor();
		f.setEmpresa( empresa );
		
		ExampleMatcher em = ExampleMatcher.matching()
				.withMatcher( "empresa", ExampleMatcher.GenericPropertyMatchers.ignoreCase() );
		
		Example<Fornecedor> ex = Example.of( f, em );
		
		return fornecedorRepository.exists( ex );
	}
	
	public void deleta( Long id ) throws FornecedorNaoEncontradoException {
		if ( !fornecedorRepository.existsById( id ) )
			throw new FornecedorNaoEncontradoException();
		
		fornecedorRepository.deleteById( id ); 
	}
		
}
