package italo.siserp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import italo.siserp.component.builder.EmpresaBuilder;
import italo.siserp.exception.EmpresaJaExisteException;
import italo.siserp.exception.EmpresaNaoEncontradaException;
import italo.siserp.model.Empresa;
import italo.siserp.model.request.BuscaEmpresasRequest;
import italo.siserp.model.request.SaveEmpresaRequest;
import italo.siserp.model.response.EmpresaResponse;
import italo.siserp.model.response.IdResponse;
import italo.siserp.repository.EmpresaRepository;

@Service
public class EmpresaService {

	@Autowired
	private EmpresaRepository empresaRepository;
				
	@Autowired
	private EmpresaBuilder empresaBuilder;
				
	public IdResponse registraEmpresa( SaveEmpresaRequest request ) throws EmpresaJaExisteException {
		if ( this.existeEmpresa( request.getRazaoSocial() ) )
			throw new EmpresaJaExisteException();
		
		Empresa e = empresaBuilder.novoEmpresa();
		empresaBuilder.carregaEmpresa( e, request );
		
		empresaRepository.save( e );
		
		return new IdResponse( e.getId() );
	}
	
	public void atualizaEmpresa( Long id, SaveEmpresaRequest request ) 
			throws EmpresaJaExisteException, EmpresaNaoEncontradaException {
		
		Empresa e = empresaRepository.findById( id ).orElseThrow( EmpresaNaoEncontradaException::new );
		
		if ( !e.getRazaoSocial().equalsIgnoreCase( request.getRazaoSocial() ) )
			if ( this.existeEmpresa( request.getRazaoSocial() ) )
				throw new EmpresaJaExisteException();
		
		e.setRazaoSocial( request.getRazaoSocial() );
		
		empresaRepository.save( e );
	}
	
	public List<EmpresaResponse> filtraEmpresas( BuscaEmpresasRequest request ) {
		Empresa e = new Empresa();
		e.setRazaoSocial( request.getRazaoSocialIni() );		
		
		ExampleMatcher em = ExampleMatcher.matching()
				.withMatcher( "razaoSocial", ExampleMatcher.GenericPropertyMatchers.ignoreCase().startsWith() );
		
		Example<Empresa> ex = Example.of( e, em );
		
		List<Empresa> empresas = empresaRepository.findAll( ex );
		
		List<EmpresaResponse> responses = new ArrayList<>();
		for( Empresa emp : empresas ) {
			EmpresaResponse resp = empresaBuilder.novoEmpresaResponse();
			empresaBuilder.carregaEmpresaResponse( resp, emp );
			
			responses.add( resp );
		}
		return responses;
	}
	
	public EmpresaResponse getEmpresa( Long id ) throws EmpresaNaoEncontradaException {				
		Empresa emp = empresaRepository.findById( id ).orElseThrow( EmpresaNaoEncontradaException::new ); 
				
		EmpresaResponse resp = empresaBuilder.novoEmpresaResponse();
		empresaBuilder.carregaEmpresaResponse( resp, emp );
			
		return resp;
	}
	
	public boolean existeEmpresa( String razaoSocial ) {
		Empresa e = new Empresa();
		e.setRazaoSocial( razaoSocial );		
		
		ExampleMatcher em = ExampleMatcher.matching()
				.withMatcher( "razaoSocial", ExampleMatcher.GenericPropertyMatchers.ignoreCase() );
		
		Example<Empresa> ex = Example.of( e, em );
		
		return empresaRepository.exists( ex );
	}
	
	public void deletaEmpresa( Long id ) throws EmpresaNaoEncontradaException {
		boolean existe = empresaRepository.existsById( id );
		if ( !existe )
			throw new EmpresaNaoEncontradaException();
		
		empresaRepository.deleteById( id ); 		
	}
	
}
