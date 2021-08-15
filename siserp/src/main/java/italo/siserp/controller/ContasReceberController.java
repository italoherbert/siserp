package italo.siserp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.siserp.exception.DataIniAposDataFimException;
import italo.siserp.exception.DataFimInvalidaException;
import italo.siserp.exception.DataIniInvalidaException;
import italo.siserp.model.request.BuscaContasReceberRequest;
import italo.siserp.model.response.ContasReceberResponse;
import italo.siserp.model.response.ErroResponse;
import italo.siserp.service.ContasReceberService;

@RestController
@RequestMapping(value="/api/conta/receber")
public class ContasReceberController {

	@Autowired
	private ContasReceberService contasReceberService;
	
	@PreAuthorize("hasAuthority('contasReceberREAD')")
	@PostMapping(value="/filtra")
	public ResponseEntity<Object> filtra( @RequestBody BuscaContasReceberRequest request ) {
		if ( request.getDataIni() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_INI_OBRIGATORIA ) );
		if ( request.getDataIni().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_INI_OBRIGATORIA ) );
		if ( request.getDataFim() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_FIM_OBRIGATORIA ) );
		if ( request.getDataFim().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_FIM_OBRIGATORIA ) );
		if ( request.getIncluirCliente() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FLAG_INCLUIR_CLIENTE_OBRIGATORIO ) );
		
		if ( request.getIncluirCliente().equals( "true" ) ) {
			if ( request.getClienteNomeIni() == null )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CLIENTE_NOME_OBRIGATORIO ) );
			if ( request.getClienteNomeIni().isBlank() )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CLIENTE_NOME_OBRIGATORIO ) );
		}
		
		if ( request.getIncluirPagas() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FLAG_INCLUIR_VENDAS_PAGAS_OBRIGATORIO ) );
				
		try {
			ContasReceberResponse resp = contasReceberService.filtra( request );
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

