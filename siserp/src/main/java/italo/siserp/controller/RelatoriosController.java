package italo.siserp.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.siserp.exception.GeracaoRelatorioException;
import italo.siserp.service.RelatorioService;
import italo.siserp.service.response.ErroResponse;

@RestController
@RequestMapping(value="/api/relatorio")
class RelatoriosController {
		
	@Autowired
	private RelatorioService relatorioService;
	
	@GetMapping(value="/balanco-dia")
	public ResponseEntity<Object> geraRelatorio( HttpServletResponse response ) {		
		try {
			relatorioService.geraRelatorioBalancoDia( response );
			return ResponseEntity.ok().build();
		} catch (GeracaoRelatorioException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FALHA_GERACAO_RELATORIO ) );
		}
	}
}
