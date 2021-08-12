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

import italo.siserp.exception.ClienteNaoEncontradoException;
import italo.siserp.exception.DataFimAposDataIniException;
import italo.siserp.exception.DataFimInvalidaException;
import italo.siserp.exception.DataIniInvalidaException;
import italo.siserp.exception.ValorRecebidoInvalidoException;
import italo.siserp.model.request.BuscaContasReceberRequest;
import italo.siserp.model.request.ValorRecebidoRequest;
import italo.siserp.model.response.ContasReceberResponse;
import italo.siserp.model.response.ErroResponse;
import italo.siserp.service.ContasReceberService;

@RestController
@RequestMapping(value="/api/conta/receber")
public class ContasReceberController {

	@Autowired
	private ContasReceberService contasReceberService;
	
	@PreAuthorize("hasAuthority('contasReceberWRITE')")
	@PatchMapping(value="/efetuarecebimento/{clienteId}")
	public ResponseEntity<Object> efetuaRecebimento( 
			@PathVariable Long clienteId, @RequestBody ValorRecebidoRequest request ) {
		
		if ( request.getValor() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.VALOR_RECEBIDO_OBRIGATORIO ) );
		if ( request.getValor().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.VALOR_RECEBIDO_OBRIGATORIO ) );
		

		try {
			contasReceberService.efetuaRecebimento( clienteId, request );
			return ResponseEntity.ok().build();
		} catch (ClienteNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CLIENTE_NAO_ENCONTRADO ) );
		} catch (ValorRecebidoInvalidoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.VALOR_RECEBITO_INVALIDO ) );
		}
	
	}
	
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
		
		try {
			ContasReceberResponse resp = contasReceberService.filtra( request );
			return ResponseEntity.ok( resp );
		} catch (DataIniInvalidaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_INI_INVALIDA ) );
		} catch (DataFimInvalidaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_FIM_INVALIDA ) );
		} catch (DataFimAposDataIniException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_INI_APOS_DATA_FIM ) );
		}
	}
		
}

