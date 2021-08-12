package italo.siserp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.siserp.exception.DataFimAposDataIniException;
import italo.siserp.exception.DataFimInvalidaException;
import italo.siserp.exception.DataIniInvalidaException;
import italo.siserp.exception.ParcelaNaoEncontradaException;
import italo.siserp.model.request.BuscaContasPagarRequest;
import italo.siserp.model.request.PagamentoParcelaRequest;
import italo.siserp.model.response.ContaPagarResponse;
import italo.siserp.model.response.ErroResponse;
import italo.siserp.service.ContaPagarService;

@RestController
@RequestMapping(value="/api/conta/pagar")
public class ContaPagarController {

	@Autowired
	private ContaPagarService compraParcelaService;
	
	@PreAuthorize("hasAuthority('contasPagarWRITE')")
	@PatchMapping(value="/altera/situacao/{parcelaId}")
	public ResponseEntity<Object> alteraParcelaSituacao( 
			@PathVariable Long parcelaId, @RequestBody PagamentoParcelaRequest request ) {
		
		if ( request.getPaga() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.VALOR_PARCELA_SITUACAO_INVALIDO ) );
		if ( request.getPaga().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.VALOR_PARCELA_SITUACAO_INVALIDO ) );
		
		try {
			compraParcelaService.alteraParcelaSituacao( parcelaId, request );
			return ResponseEntity.ok().build();
		} catch (ParcelaNaoEncontradaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PARCELA_NAO_ENCONTRADA ) );
		}
	}
	
	@PreAuthorize("hasAuthority('contasPagarREAD')")
	@PostMapping(value="/filtra")
	public ResponseEntity<Object> filtra( @RequestBody BuscaContasPagarRequest request ) {
		if ( request.getDataIni() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_INI_OBRIGATORIA ) );
		if ( request.getDataIni().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_INI_OBRIGATORIA ) );
		if ( request.getDataFim() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_FIM_OBRIGATORIA ) );
		if ( request.getDataFim().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_FIM_OBRIGATORIA ) );
		
		try {
			List<ContaPagarResponse> lista = compraParcelaService.filtra( request );
			return ResponseEntity.ok( lista );
		} catch (DataIniInvalidaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_INI_INVALIDA ) );
		} catch (DataFimInvalidaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_FIM_INVALIDA ) );
		} catch (DataFimAposDataIniException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_INI_APOS_DATA_FIM ) );
		}
	}
	
}
