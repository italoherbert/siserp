package italo.siserp.builder;

import org.springframework.stereotype.Component;

import italo.siserp.model.Config;
import italo.siserp.model.request.SaveConfigSemLogoRequest;
import italo.siserp.model.request.SaveLogoConfigRequest;
import italo.siserp.model.response.ConfigLogoResponse;

@Component
public class ConfigBuilder {

	public void carregaConfigSemLogo( Config cfg, SaveConfigSemLogoRequest req ) {
	}
	
	public void carregaLogoBase64Config( Config cfg, SaveLogoConfigRequest req ) {
		cfg.setLogoBase64( req.getLogoBase64() ); 
	}
		
	public void carregaLogoBase64Response( ConfigLogoResponse resp, Config cfg ) {
		resp.setLogoBase64( cfg.getLogoBase64() );
	}	
	
	public ConfigLogoResponse novoConfigResponse() {
		return new ConfigLogoResponse();
	}
	
	public Config novoConfig() {
		return new Config();
	}
}
