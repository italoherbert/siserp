package italo.siserp.service;

import org.springframework.stereotype.Service;

import italo.siserp.service.request.SaveSedeRequest;
import italo.siserp.service.response.SedeResponse;

@Service
public class SedeService {
		
	private String cnpj = "0000.0000.000/0000-0";
	
	private String inscricaoEstadual = "0000000-00";
	
	public void salvaSede( SaveSedeRequest request ) {
			
	}
		
	public SedeResponse getSede() {						
		SedeResponse resp = new SedeResponse();
		resp.setCnpj( cnpj );
		resp.setInscricaoEstadual( inscricaoEstadual ); 
		return resp;
	}
		
	
}
