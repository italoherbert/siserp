package italo.siserp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.siserp.exception.DataIniAposDataFimException;
import italo.siserp.exception.DataFimInvalidaException;
import italo.siserp.exception.DataIniInvalidaException;
import italo.siserp.exception.ParcelaNaoEncontradaException;
import italo.siserp.service.ContasPagarService;
import italo.siserp.service.request.BuscaContasPagarRequest;
import italo.siserp.service.request.PagamentoParcelaRequest;
import italo.siserp.service.response.ContasPagarResponse;
import italo.siserp.service.response.ErroResponse;

@RestController
@RequestMapping(value="/api/conta/pagar")
public class ContasPagarController {

	@Autowired
	private ContasPagarService contasPagarService;
	
	@PreAuthorize("hasAuthority('contasPagarWRITE')")
	@PatchMapping(value="/altera/situacao/{parcelaId}")
	public ResponseEntity<Object> alteraParcelaSituacao( 
			@PathVariable Long parcelaId, @RequestBody PagamentoParcelaRequest request ) {
		
		if ( request.getPaga() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.VALOR_PARCELA_SITUACAO_INVALIDO ) );
		if ( request.getPaga().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.VALOR_PARCELA_SITUACAO_INVALIDO ) );
		
		try {
			contasPagarService.alteraParcelaSituacao( parcelaId, request );
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
		
		if ( request.getIncluirFornecedor() == null ) 
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FORNECEDOR_EMPRESA_OBRIGATORIA ) );
		
		if ( request.getIncluirFornecedor().equals( "true" ) ) {
			if ( request.getFornecedorNomeIni() == null )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FORNECEDOR_EMPRESA_OBRIGATORIA ) );
			if ( request.getFornecedorNomeIni().isBlank() )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FORNECEDOR_EMPRESA_OBRIGATORIA ) );
		}
		
		if ( request.getIncluirPagas() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FLAG_INCLUIR_PARCELAS_PAGAS_OBRIGATORIO ) );
		
		try {			
			ContasPagarResponse resp = contasPagarService.filtra( request );
			return ResponseEntity.ok( resp );
		} catch (DataIniInvalidaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_INI_INVALIDA ) );
		} catch (DataFimInvalidaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_FIM_INVALIDA ) );
		} catch (DataIniAposDataFimException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_INI_APOS_DATA_FIM ) );
		}
	}
		
}
