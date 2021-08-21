package italo.siserp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.siserp.exception.FalhaCarregamentoArquivoException;
import italo.siserp.exception.FalhaCriacaoArquivoException;
import italo.siserp.exception.FalhaGravacaoArquivoException;
import italo.siserp.service.SedeService;
import italo.siserp.service.request.SaveSedeRequest;
import italo.siserp.service.response.ErroResponse;
import italo.siserp.service.response.SedeResponse;

@RestController
@RequestMapping(value="/api/sede")
public class SedeController {
	
	@Autowired
	private SedeService sedeService;
			
	@PreAuthorize("hasAuthority('sedeWRITE')")	
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
		
		try {
			sedeService.salvaSede( request );
		} catch (FalhaCriacaoArquivoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FALHA_CRIACAO_ARQUIVO ) );
		} catch (FalhaCarregamentoArquivoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FALHA_CARREGAMENTO_ARQUIVO ) );
		} catch (FalhaGravacaoArquivoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FALHA_GRAVACAO_ARQUIVO ) );
		}
		return ResponseEntity.ok().build();		
	}
	
	
	@PreAuthorize("hasAuthority('sedeREAD')")	
	@GetMapping(value="/get")
	public ResponseEntity<Object> get() {
		try {
			SedeResponse resp = sedeService.getSede();
			return ResponseEntity.ok( resp );
		} catch (FalhaCarregamentoArquivoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FALHA_CARREGAMENTO_ARQUIVO ) );
		}
	}
		
}

