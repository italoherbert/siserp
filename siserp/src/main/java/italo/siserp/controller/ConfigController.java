package italo.siserp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.siserp.service.ConfigService;
import italo.siserp.service.request.SaveLogoConfigRequest;
import italo.siserp.service.response.ConfigLogoResponse;
import italo.siserp.service.response.ErroResponse;

@RestController
@RequestMapping(value="/api/config")
public class ConfigController {

	@Autowired
	private ConfigService configService;
			
	@PreAuthorize("hasAuthority('configWRITE')")
	@PutMapping(value="/logo/salva")	
	public ResponseEntity<Object> salvaLogo( @RequestBody SaveLogoConfigRequest request ) {
		if ( request.getLogoBase64() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.LOGO_BASE64_OBRIGATORIA ) );
		if ( request.getLogoBase64().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.LOGO_BASE64_OBRIGATORIA ) );
		
		configService.salvaLogoBase64( request );
		return ResponseEntity.ok().build();
	}
	
	@GetMapping(value="/logo/get")	
	public ResponseEntity<Object> getLogo() {
		ConfigLogoResponse resp = configService.buscaLogo();
		return ResponseEntity.ok( resp );
	}
		
}
