package italo.siserp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.siserp.exception.CaixaJaAbertoException;
import italo.siserp.exception.CaixaNaoAbertoException;
import italo.siserp.exception.CaixaNaoEncontradoException;
import italo.siserp.exception.CaixaValorInicialInvalidoException;
import italo.siserp.exception.DataFimAposDataIniException;
import italo.siserp.exception.DataFimInvalidaException;
import italo.siserp.exception.DataIniInvalidaException;
import italo.siserp.exception.FuncionarioNaoEncontradoException;
import italo.siserp.exception.LancamentoTipoInvalidoException;
import italo.siserp.exception.LancamentoValorInvalidoException;
import italo.siserp.exception.PerfilCaixaRequeridoException;
import italo.siserp.exception.UsuarioNaoEncontradoException;
import italo.siserp.model.request.AbreCaixaRequest;
import italo.siserp.model.request.BuscaCaixasRequest;
import italo.siserp.model.request.SaveLancamentoRequest;
import italo.siserp.model.response.CaixaResponse;
import italo.siserp.model.response.ErroResponse;
import italo.siserp.service.CaixaService;

@RestController
@RequestMapping(value="/api/caixa")
public class CaixaController {

	@Autowired
	private CaixaService caixaService;
	
	@PostMapping(value="/abre/{usuarioId}")
	public ResponseEntity<Object> abrirCaixa( 
			@PathVariable Long usuarioId, @RequestBody AbreCaixaRequest request ) {
		
		if ( request.getLancamento() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.LANCAMENTO_ABERTURA_CAIXA_OBRITATORIO ) );		
		
		if ( request.getLancamento().getValor() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.VALOR_LANCAMENTO_OBRIGATORIO ) );		
		if ( request.getLancamento().getValor().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.VALOR_LANCAMENTO_OBRIGATORIO ) );		
		
		try {
			caixaService.abreGetCaixaSeNaoAberto( usuarioId, request );
			return ResponseEntity.ok().build();
		} catch (PerfilCaixaRequeridoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PERFIL_DE_CAIXA_REQUEERIDO ) );		
		} catch (CaixaValorInicialInvalidoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CAIXA_VALOR_INICIAL_INVALIDO ) );		
		} catch (CaixaJaAbertoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CAIXA_JA_ABERTO ) );					
		} catch (LancamentoTipoInvalidoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.LANCAMENTO_TIPO_INVALIDO ) );					
		} catch (LancamentoValorInvalidoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.LANCAMENTO_VALOR_INVALIDO ) );					
		} catch (UsuarioNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_NAO_ENCONTRADO ) );					
		} catch (FuncionarioNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FUNCIONARIO_NAO_ENCONTRADO ) );					
		}		
	}
	
	@PostMapping(value="/efetua/lancamento/{usuarioId}")
	public ResponseEntity<Object> efetuarLancamento( @PathVariable Long usuarioId, @RequestBody SaveLancamentoRequest request ) {
		try {
			caixaService.efetuaLancamento( usuarioId, request );
			return ResponseEntity.ok().build();
		} catch (PerfilCaixaRequeridoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PERFIL_DE_CAIXA_REQUEERIDO ) );		
		} catch (CaixaNaoAbertoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CAIXA_NAO_ABERTO ) );		
		} catch (LancamentoTipoInvalidoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.LANCAMENTO_TIPO_INVALIDO ) );		
		} catch (LancamentoValorInvalidoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.LANCAMENTO_VALOR_INVALIDO ) );		
		} catch (UsuarioNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_NAO_ENCONTRADO ) );					
		} catch (FuncionarioNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FUNCIONARIO_NAO_ENCONTRADO ) );					
		}
	}
	
	@GetMapping(value="/get/uid/hoje/{usuarioId}")
	public ResponseEntity<Object> buscaPorUsuarioID( @PathVariable Long usuarioId ) {		
		try {
			CaixaResponse resp = caixaService.buscaHojeCaixa( usuarioId );
			return ResponseEntity.ok( resp );
		} catch (PerfilCaixaRequeridoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PERFIL_DE_CAIXA_REQUEERIDO ) );		
		} catch (CaixaNaoAbertoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CAIXA_NAO_ABERTO ) );					
		} catch (UsuarioNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_NAO_ENCONTRADO ) );					
		} catch (FuncionarioNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FUNCIONARIO_NAO_ENCONTRADO ) );					
		}
	}
	
	@PostMapping(value="/filtra")
	public ResponseEntity<Object> filtra( @RequestBody BuscaCaixasRequest request ) {
		try {
			List<CaixaResponse> lista = caixaService.filtra( request );
			return ResponseEntity.ok( lista );
		} catch (DataIniInvalidaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_INI_INVALIDA ) );					
		} catch (DataFimInvalidaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_FIM_INVALIDA ) );					
		} catch (DataFimAposDataIniException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_INI_APOS_DATA_FIM ) );					
		}
	}
		
	@DeleteMapping(value="/deleta/{caixaId}")
	public ResponseEntity<Object> deleta( @PathVariable Long caixaId ) {
		try {
			caixaService.deleteCaixa( caixaId );
			return ResponseEntity.ok().build();
		} catch (CaixaNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CAIXA_NAO_ENCONTRADO ) );					
		}
	}
	
}
