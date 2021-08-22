package italo.siserp.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.siserp.builder.ConfigBuilder;
import italo.siserp.exception.FalhaLeituraLogoException;
import italo.siserp.model.Config;
import italo.siserp.repository.ConfigRepository;
import italo.siserp.service.request.SaveLogoConfigRequest;
import italo.siserp.service.response.ConfigLogoResponse;
import italo.siserp.util.ImagemUtil;

@Service
public class ConfigService {
	
	private final int IMAGEM_AJUSTE_DIM = 64;

	@Autowired
	private ConfigRepository configRepository;
	
	@Autowired
	private ConfigBuilder configBuilder;
	
	@Autowired
	private ImagemUtil imagemUtil;
	
	public ConfigLogoResponse salvaLogoBase64( SaveLogoConfigRequest request ) throws FalhaLeituraLogoException {		
		try {			
			String base64 = request.getLogoBase64();
			BufferedImage image = imagemUtil.base64ToImage( base64 );
			BufferedImage novaImage = imagemUtil.ajustaImagem( image, IMAGEM_AJUSTE_DIM );
			
			request.setLogoBase64( imagemUtil.imageToBase64( novaImage ) );
			
			Config cfg = this.getConfigBean();			
			configBuilder.carregaLogoBase64Config( cfg, request ); 						
			
			configRepository.save( cfg );
			
			ConfigLogoResponse resp = configBuilder.novoConfigResponse();
			configBuilder.carregaLogoBase64Response( resp, cfg ); 
			return resp;
		} catch ( IOException e ) {
			throw new FalhaLeituraLogoException();
		}		
	}
					
	public ConfigLogoResponse buscaLogo() {
		List<Config> lista = configRepository.findAll();

		ConfigLogoResponse resp = configBuilder.novoConfigResponse();
		if ( lista.isEmpty() ) {
			resp.setLogoBase64( null ); 
		} else {
			Config cfg = lista.get( 0 );
			configBuilder.carregaLogoBase64Response( resp, cfg );
		}
		return resp;
	}			
	
	public Config getConfigBean() {
		List<Config> lista = configRepository.findAll();
		
		if ( !lista.isEmpty() )
			return lista.get( 0 );		
		return new Config();
	}
	
}
