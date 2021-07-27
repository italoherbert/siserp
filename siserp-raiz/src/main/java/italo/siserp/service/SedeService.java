package italo.siserp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import italo.siserp.component.builder.SedeBuilder;
import italo.siserp.exception.SedeJaExisteException;
import italo.siserp.exception.SedeNaoEncontradaException;
import italo.siserp.model.Sede;
import italo.siserp.model.request.BuscaSedesRequest;
import italo.siserp.model.request.SaveSedeRequest;
import italo.siserp.model.response.IdResponse;
import italo.siserp.model.response.SedeResponse;
import italo.siserp.repository.SedeRepository;

@Service
public class SedeService {

	@Autowired
	private SedeRepository sedeRepository;
	
	@Autowired
	private SedeBuilder sedeBuilder;
	
	public IdResponse registraSede( Long empresaId, SaveSedeRequest request ) throws SedeJaExisteException {
		if ( this.existeSede( request.getCnpj() ) )
			throw new SedeJaExisteException();
		
		Sede sede = sedeBuilder.novoSede( empresaId );
		sede.setCnpj( request.getCnpj() );
		sede.setInscricaoEstadual( request.getInscricaoEstadual() ); 
		
		sedeRepository.save( sede );
		
		return new IdResponse( sede.getId() );
	}
	
	public void atualizaSede( Long id, SaveSedeRequest request ) 
			throws SedeJaExisteException, SedeNaoEncontradaException {
		
		Sede sede = sedeRepository.findById( id ).orElseThrow( SedeNaoEncontradaException::new );
		
		if ( !sede.getCnpj().equalsIgnoreCase( request.getCnpj() ) )
			if ( this.existeSede( request.getCnpj() ) )
				throw new SedeJaExisteException();
		
		sede.setCnpj( request.getCnpj() );
		sede.setInscricaoEstadual( request.getInscricaoEstadual() );
		
		sedeRepository.save( sede );
	}
	
	public List<SedeResponse> filtraSedes( BuscaSedesRequest request ) {
		Sede sede = new Sede();
		sede.setCnpj( request.getCnpjIni() );		
		sede.setInscricaoEstadual( request.getInscricaoEstadualIni() );
		
		ExampleMatcher em = ExampleMatcher.matchingAny()
				.withMatcher( "cnpj", ExampleMatcher.GenericPropertyMatchers.ignoreCase().startsWith() )
				.withMatcher( "inscricaoEstadual", ExampleMatcher.GenericPropertyMatchers.ignoreCase().startsWith() );
		
		Example<Sede> ex = Example.of( sede, em );
		
		List<Sede> Sedes = sedeRepository.findAll( ex );
		
		List<SedeResponse> responses = new ArrayList<>();
		for( Sede s : Sedes ) {
			SedeResponse resp = sedeBuilder.novoSedeResponse();
			sedeBuilder.carregaSedeResponse( resp, s );
			
			responses.add( resp );
		}
		return responses;
	}
	
	public SedeResponse getSede( Long id ) throws SedeNaoEncontradaException {				
		Sede sede = sedeRepository.findById( id ).orElseThrow( SedeNaoEncontradaException::new ); 
				
		SedeResponse resp = sedeBuilder.novoSedeResponse();
		sedeBuilder.carregaSedeResponse( resp, sede );
			
		return resp;
	}
	
	public boolean existeSede( String cnpj ) {
		Sede sede = new Sede();
		sede.setCnpj( cnpj );		
		
		ExampleMatcher em = ExampleMatcher.matching()
				.withMatcher( "cnpj", ExampleMatcher.GenericPropertyMatchers.ignoreCase() );
		
		Example<Sede> ex = Example.of( sede, em );
		
		return sedeRepository.exists( ex );
	}
	
	public void deletaSede( Long id ) throws SedeNaoEncontradaException {
		boolean existe = sedeRepository.existsById( id );
		if ( !existe )
			throw new SedeNaoEncontradaException();
		
		sedeRepository.deleteById( id ); 		
	}
	
	
}
