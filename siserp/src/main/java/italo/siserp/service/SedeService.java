package italo.siserp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.siserp.builder.SedeBuilder;
import italo.siserp.model.Sede;
import italo.siserp.model.request.SaveSedeRequest;
import italo.siserp.model.response.SedeResponse;
import italo.siserp.repository.SedeRepository;

@Service
public class SedeService {
			
	@Autowired
	private SedeRepository sedeRepository;
	
	@Autowired
	private SedeBuilder sedeBuilder;
	
	public void salvaSede( SaveSedeRequest request ) {
		Sede sede = this.getSedeBean();
		sedeBuilder.carregaSede( sede, request );
		
		sedeRepository.save( sede );
	}
		
	public SedeResponse getSede() {
		Sede sede = this.getSedeBean();
		
		SedeResponse resp = new SedeResponse();
		sedeBuilder.carregaSedeResponse( resp, sede );
		
		return resp;
	}
		
	public Sede getSedeBean() {
		List<Sede> lista = sedeRepository.findAll();
		if ( lista.isEmpty() )		
			return new Sede();
		
		return lista.get( 0 );	
	}
	
}
