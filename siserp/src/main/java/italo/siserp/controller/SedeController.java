package italo.siserp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.siserp.model.request.SaveSedeRequest;
import italo.siserp.model.response.ErroResponse;
import italo.siserp.model.response.SedeResponse;
import italo.siserp.service.SedeService;

@RestController
@RequestMapping(value="/api/sede")
public class SedeController {
	
	@Autowired
	private SedeService sedeService;
			
	@PreAuthorize("hasAnyAuthority('ADMIN', 'GERENTE')")
	@PutMapping(value="/salva")
	public ResponseEntity<Object> salvaSede( @RequestBody SaveSedeRequest request ) {		
		if ( request.getCnpj() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CNPJ_OBRIGATORIO ) );
		if ( request.getCnpj().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CNPJ_OBRIGATORIO ) );
		
		if ( request.getInscricaoEstadual() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.INSCRICAO_ESTADUAL_OBRIGATORIA ) );
		if ( request.getInscricaoEstadual().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.INSCRICAO_ESTADUAL_OBRIGATORIA ) );
		
		sedeService.salvaSede( request );
		return ResponseEntity.ok().build();		
	}
	
	
	@PreAuthorize("hasAnyAuthority('ADMIN', 'GERENTE', 'CAIXA')")	
	@GetMapping(value="/get")
	public ResponseEntity<Object> get() {		
		SedeResponse resp = sedeService.getSede();
		return ResponseEntity.ok( resp );			
	}
		
}

