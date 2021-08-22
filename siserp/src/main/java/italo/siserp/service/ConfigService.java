package italo.siserp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.siserp.builder.ConfigBuilder;
import italo.siserp.model.Config;
import italo.siserp.repository.ConfigRepository;
import italo.siserp.service.request.SaveLogoConfigRequest;
import italo.siserp.service.response.ConfigLogoResponse;

@Service
public class ConfigService {

	@Autowired
	private ConfigRepository configRepository;
	
	@Autowired
	private ConfigBuilder configBuilder;
	
	public void salvaLogoBase64( SaveLogoConfigRequest request ) {		
		Config cfg = this.getConfigBean();			
		configBuilder.carregaLogoBase64Config( cfg, request ); 
		
		configRepository.save( cfg );
	}
					
	public ConfigLogoResponse buscaLogo() {
		Config cfg = this.getConfigBean();		
		
		ConfigLogoResponse resp = new ConfigLogoResponse();
		configBuilder.carregaLogoBase64Response( resp, cfg ); 
		return resp;
	}			
	
	public Config getConfigBean() {
		List<Config> lista = configRepository.findAll();
		
		if ( !lista.isEmpty() )
			return lista.get( 0 );		
		return new Config();
	}
	
}
