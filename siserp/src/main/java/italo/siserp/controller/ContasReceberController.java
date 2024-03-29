package italo.siserp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.siserp.exception.CaixaNaoAbertoException;
import italo.siserp.exception.ClienteNaoEncontradoException;
import italo.siserp.exception.DataFimInvalidaException;
import italo.siserp.exception.DataIniAposDataFimException;
import italo.siserp.exception.DataIniInvalidaException;
import italo.siserp.exception.FuncionarioNaoEncontradoException;
import italo.siserp.exception.LongInvalidoException;
import italo.siserp.exception.ParcelaNaoEncontradaException;
import italo.siserp.exception.PerfilCaixaRequeridoException;
import italo.siserp.exception.UsuarioLogadoNaoEncontradoException;
import italo.siserp.exception.ValorRecebidoInvalidoException;
import italo.siserp.model.request.BuscaContasReceberRequest;
import italo.siserp.model.request.EfetuarRecebimentoRequest;
import italo.siserp.model.response.ContasReceberResponse;
import italo.siserp.model.response.ErroResponse;
import italo.siserp.model.response.RecebimentoEfetuadoResponse;
import italo.siserp.service.ContasReceberService;

@RestController
@RequestMapping(value="/api/conta/receber")
public class ContasReceberController {

	@Autowired
	private ContasReceberService contasReceberService;
	
	@PreAuthorize("hasAuthority('vendaWRITE')")
	@PostMapping(value="/efetuarecebimento")
	public ResponseEntity<Object> efetuaRecebimento( @RequestHeader Long logadoUID, @RequestBody EfetuarRecebimentoRequest request ) {
		
		if ( request.getClienteId() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CLIENTE_NAO_ENCONTRADO ) );
		if ( request.getClienteId().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CLIENTE_NAO_ENCONTRADO ) );
		
		try {
			RecebimentoEfetuadoResponse resp = contasReceberService.efetuaRecebimento( logadoUID, request );
			return ResponseEntity.ok( resp );
		} catch (ClienteNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CLIENTE_NAO_ENCONTRADO ) );
		} catch (ValorRecebidoInvalidoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.VALOR_RECEBIDO_INVALIDO ) );
		} catch (PerfilCaixaRequeridoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PERFIL_DE_CAIXA_REQUERIDO ) );
		} catch (CaixaNaoAbertoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CAIXA_NAO_ABERTO ) );			
		} catch (UsuarioLogadoNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_LOGADO_NAO_ENCONTRADO ) );			
		} catch (FuncionarioNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FUNCIONARIO_NAO_ENCONTRADO ) );			
		} catch (LongInvalidoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CLIENTE_NAO_ENCONTRADO ) );						
		}
	}
	
	@PreAuthorize("hasAuthority('contasReceberWRITE')")
	@PostMapping(value="/restauradebito/{parcelaId}")
	public ResponseEntity<Object> restauraDebito( @PathVariable Long parcelaId ) {
		try {
			contasReceberService.restauraDebito( parcelaId );
			return ResponseEntity.ok().build();
		} catch (ParcelaNaoEncontradaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PARCELA_NAO_ENCONTRADA ) );						
		}
	}
	
	@PreAuthorize("hasAuthority('contasReceberWRITE')")
	@PostMapping(value="/restaurarecebimento/{parcelaId}")
	public ResponseEntity<Object> restauraRecebimento( @PathVariable Long parcelaId ) {
		try {
			contasReceberService.restauraRecebimento( parcelaId );
			return ResponseEntity.ok().build();
		} catch (ParcelaNaoEncontradaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PARCELA_NAO_ENCONTRADA ) );						
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

