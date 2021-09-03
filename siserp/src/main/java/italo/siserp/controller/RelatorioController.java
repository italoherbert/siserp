package italo.siserp.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.siserp.exception.GeracaoRelatorioException;
import italo.siserp.model.response.ErroResponse;
import italo.siserp.service.RelatorioService;

@RestController
@RequestMapping(value="/api/relatorio")
class RelatorioController {
		
	@Autowired
	private RelatorioService relatorioService;
	
	@PreAuthorize("hasAuthority('caixaREAD')")	
	@GetMapping(value="/balanco-hoje")
	public ResponseEntity<Object> geraRelatorio( HttpServletResponse response ) {		
		try {
			relatorioService.geraRelatorioBalancoHoje( response );
			return ResponseEntity.ok().build();
		} catch (GeracaoRelatorioException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FALHA_GERACAO_RELATORIO ) );
		}
	}
}
