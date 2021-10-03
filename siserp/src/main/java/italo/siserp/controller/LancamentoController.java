package italo.siserp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.siserp.exception.CaixaNaoAbertoException;
import italo.siserp.exception.CaixaNaoEncontradoException;
import italo.siserp.exception.FuncionarioNaoEncontradoException;
import italo.siserp.exception.LancamentoNaoEncontradoException;
import italo.siserp.exception.LancamentoTipoInvalidoException;
import italo.siserp.exception.LancamentoValorInvalidoException;
import italo.siserp.exception.PerfilCaixaRequeridoException;
import italo.siserp.exception.UsuarioLogadoNaoEncontradoException;
import italo.siserp.exception.ValorEmCaixaInsuficienteException;
import italo.siserp.model.request.SaveLancamentoRequest;
import italo.siserp.model.response.ErroResponse;
import italo.siserp.model.response.LancamentoResponse;
import italo.siserp.service.LancamentoService;
import italo.siserp.util.LancamentoEnumConversor;

@RestController
@RequestMapping(value="/api/lancamento")
public class LancamentoController {
	
	@Autowired
	private LancamentoService lancamentoService;
			
	@Autowired
	private LancamentoEnumConversor lancamentoTipoEnumConversor;
				
	@PreAuthorize("hasAuthority('lancamentoWRITE')")	
	@PostMapping(value="/novo/hoje")
	public ResponseEntity<Object> efetuarLancamento( @RequestHeader Long logadoUID, @RequestBody SaveLancamentoRequest request ) {		
		if ( request.getTipo() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.TIPO_LANCAMENTO_OBRIGATORIO ) );		
		if ( request.getTipo().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.TIPO_LANCAMENTO_OBRIGATORIO ) );		
		
		if ( request.getValor() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.VALOR_LANCAMENTO_OBRIGATORIO ) );		
		if ( request.getValor().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.VALOR_LANCAMENTO_OBRIGATORIO ) );		
				
		try {			
			lancamentoService.efetuaLancamento( logadoUID, request );
			return ResponseEntity.ok().build();
		} catch (PerfilCaixaRequeridoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PERFIL_DE_CAIXA_REQUERIDO ) );		
		} catch (CaixaNaoAbertoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CAIXA_NAO_ABERTO ) );		
		} catch (LancamentoTipoInvalidoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.LANCAMENTO_TIPO_INVALIDO ) );		
		} catch (LancamentoValorInvalidoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.LANCAMENTO_VALOR_INVALIDO ) );		
		} catch (UsuarioLogadoNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_LOGADO_NAO_ENCONTRADO ) );					
		} catch (FuncionarioNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FUNCIONARIO_NAO_ENCONTRADO ) );					
		} catch (ValorEmCaixaInsuficienteException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.VALOR_EM_CAIXA_INSUFICIENTE ) );					
		}
	}
	
	@PreAuthorize("hasAuthority('lancamentoREAD')")	
	@GetMapping("/lista/hoje")
	public ResponseEntity<Object> listaLancamentosPorUsuario( @RequestHeader Long logadoUID ) {
		try {			
			List<LancamentoResponse> lista = lancamentoService.buscaLancamentosPorUsuarioId( logadoUID );
			return ResponseEntity.ok( lista );
		} catch (PerfilCaixaRequeridoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PERFIL_DE_CAIXA_REQUERIDO ) );					
		} catch (CaixaNaoAbertoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CAIXA_NAO_ABERTO ) );					
		} catch (UsuarioLogadoNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_LOGADO_NAO_ENCONTRADO ) );					
		} catch (FuncionarioNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FUNCIONARIO_NAO_ENCONTRADO ) );					
		}				
	}
	
	@PreAuthorize("hasAuthority('lancamentoREAD')")	
	@GetMapping("/lista/{caixaId}")
	public ResponseEntity<Object> listaLancamentosPorCaixa( @PathVariable Long caixaId ) {
		try {			
			List<LancamentoResponse> lista = lancamentoService.buscaLancamentosPorCaixaId( caixaId );
			return ResponseEntity.ok( lista );
		} catch ( CaixaNaoEncontradoException e ) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CAIXA_NAO_ENCONTRADO ) );					
		}
	}

	@PreAuthorize("hasAuthority('lancamentoDELETE')")	
	@DeleteMapping(value="/deletatodos/hoje")
	public ResponseEntity<Object> deletaLancamentos( @RequestHeader Long logadoUID ) {
		try {			
			lancamentoService.deletaLancamentosHoje( logadoUID );
			return ResponseEntity.ok().build();
		} catch (PerfilCaixaRequeridoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PERFIL_DE_CAIXA_REQUERIDO ) );					
		} catch (CaixaNaoAbertoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CAIXA_NAO_ABERTO ) );					
		} catch (UsuarioLogadoNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_LOGADO_NAO_ENCONTRADO ) );					
		} catch (FuncionarioNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FUNCIONARIO_NAO_ENCONTRADO ) );					
		}
	}
	
	@PreAuthorize("hasAuthority('lancamentoDELETE')")	
	@DeleteMapping(value="/deleta/{id}")
	public ResponseEntity<Object> deleta( @PathVariable Long id ) {
		try {
			lancamentoService.deleta( id );
			return ResponseEntity.ok().build();
		} catch (LancamentoNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.LANCAMENTO_NAO_ENCONTRADO ) );					
		}
	}
	
	@GetMapping(value="/tipos")
	public ResponseEntity<Object> buscaTipos() {
		String[] tipos = lancamentoTipoEnumConversor.getLancamentoTipos();
		return ResponseEntity.ok( tipos );
	}
		
}
