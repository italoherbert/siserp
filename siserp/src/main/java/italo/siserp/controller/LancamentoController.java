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

import italo.siserp.exception.CaixaNaoAbertoException;
import italo.siserp.exception.FuncionarioNaoEncontradoException;
import italo.siserp.exception.LancamentoNaoEncontradoException;
import italo.siserp.exception.LancamentoTipoInvalidoException;
import italo.siserp.exception.LancamentoValorInvalidoException;
import italo.siserp.exception.PerfilCaixaRequeridoException;
import italo.siserp.exception.UsuarioNaoEncontradoException;
import italo.siserp.model.request.SaveLancamentoRequest;
import italo.siserp.model.response.CaixaBalancoResponse;
import italo.siserp.model.response.ErroResponse;
import italo.siserp.model.response.LancamentoResponse;
import italo.siserp.service.LancamentoService;
import italo.siserp.util.enums_tipo.LancamentoTipoEnumConversor;

@RestController
@RequestMapping(value="/api/lancamento")
public class LancamentoController {
	
	@Autowired
	private LancamentoService lancamentoService;
			
	@Autowired
	private LancamentoTipoEnumConversor lancamentoTipoEnumConversor;
	
	@GetMapping(value="/balanco/{usuarioId}")
	public ResponseEntity<Object> geraBalanco( @PathVariable Long usuarioId ) {
		try {						
			CaixaBalancoResponse resp = lancamentoService.geraBalanco( usuarioId );
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
	
	@PostMapping(value="/novo/hoje/{usuarioId}")
	public ResponseEntity<Object> efetuarLancamento( @PathVariable Long usuarioId, @RequestBody SaveLancamentoRequest request ) {
		if ( request.getTipo() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.TIPO_LANCAMENTO_OBRIGATORIO ) );		
		if ( request.getTipo().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.TIPO_LANCAMENTO_OBRIGATORIO ) );		
		
		if ( request.getValor() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.VALOR_LANCAMENTO_OBRIGATORIO ) );		
		if ( request.getValor().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.VALOR_LANCAMENTO_OBRIGATORIO ) );		
		
		try {			
			lancamentoService.efetuaLancamento( usuarioId, request );
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
	
	@DeleteMapping(value="/deletatodos/hoje/{usuarioId}")
	public ResponseEntity<Object> deletaLancamentos( @PathVariable Long usuarioId ) {
		try {			
			lancamentoService.deletaLancamentos( usuarioId );
			return ResponseEntity.ok().build();
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
	
	@GetMapping("/lista/hoje/{usuarioId}")
	public ResponseEntity<Object> buscaLancamentos( @PathVariable Long usuarioId ) {
		try {			
			List<LancamentoResponse> lista = lancamentoService.buscaLancamentos( usuarioId );
			return ResponseEntity.ok( lista );
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
