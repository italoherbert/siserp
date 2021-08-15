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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.siserp.exception.CaixaJaAbertoException;
import italo.siserp.exception.CaixaNaoAbertoException;
import italo.siserp.exception.CaixaNaoEncontradoException;
import italo.siserp.exception.CaixaValorInicialInvalidoException;
import italo.siserp.exception.DataDiaInvalidaException;
import italo.siserp.exception.DataFimInvalidaException;
import italo.siserp.exception.DataIniAposDataFimException;
import italo.siserp.exception.DataIniInvalidaException;
import italo.siserp.exception.FuncionarioNaoEncontradoException;
import italo.siserp.exception.LancamentoTipoInvalidoException;
import italo.siserp.exception.LancamentoValorInvalidoException;
import italo.siserp.exception.PerfilCaixaRequeridoException;
import italo.siserp.exception.UsuarioNaoEncontradoException;
import italo.siserp.model.request.AbreCaixaRequest;
import italo.siserp.model.request.BuscaBalancosDiarios;
import italo.siserp.model.request.BuscaCaixasPorDataDiaRequest;
import italo.siserp.model.request.BuscaCaixasRequest;
import italo.siserp.model.request.FechaCaixaRequest;
import italo.siserp.model.response.BalancoDiarioResponse;
import italo.siserp.model.response.CaixaBalancoResponse;
import italo.siserp.model.response.CaixaResponse;
import italo.siserp.model.response.ErroResponse;
import italo.siserp.service.CaixaService;

@RestController
@RequestMapping(value="/api/caixa")
public class CaixaController {

	@Autowired
	private CaixaService caixaService;
			
	@PreAuthorize("hasAuthority('caixaWRITE')")
	@PostMapping(value="/abre/{usuarioId}")
	public ResponseEntity<Object> abreCaixa( 
			@PathVariable Long usuarioId, @RequestBody AbreCaixaRequest request ) {
		
		if ( request.getLancamento() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.LANCAMENTO_ABERTURA_CAIXA_OBRITATORIO ) );		
		
		if ( request.getLancamento().getValor() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.VALOR_LANCAMENTO_OBRIGATORIO ) );		
		if ( request.getLancamento().getValor().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.VALOR_LANCAMENTO_OBRIGATORIO ) );		
		
		try {
			caixaService.abreCaixa( usuarioId, request );
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
	

	@PreAuthorize("hasAuthority('caixaWRITE')")
	@PostMapping(value="/fecha/{usuarioId}")
	public ResponseEntity<Object> fechaCaixa( 
			@PathVariable Long usuarioId, @RequestBody FechaCaixaRequest request ) {
		
		if ( request.getLancamento() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.LANCAMENTO_ABERTURA_CAIXA_OBRITATORIO ) );		
		
		if ( request.getLancamento().getValor() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.VALOR_LANCAMENTO_OBRIGATORIO ) );		
		if ( request.getLancamento().getValor().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.VALOR_LANCAMENTO_OBRIGATORIO ) );		
		
		try {
			caixaService.fechaCaixa( usuarioId, request );
			return ResponseEntity.ok().build();
		} catch (PerfilCaixaRequeridoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PERFIL_DE_CAIXA_REQUEERIDO ) );		
		} catch (LancamentoTipoInvalidoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.LANCAMENTO_TIPO_INVALIDO ) );					
		} catch (LancamentoValorInvalidoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.LANCAMENTO_VALOR_INVALIDO ) );					
		} catch (UsuarioNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_NAO_ENCONTRADO ) );					
		} catch (FuncionarioNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FUNCIONARIO_NAO_ENCONTRADO ) );					
		} catch (CaixaNaoAbertoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CAIXA_NAO_ABERTO ) );					
		}		
	}
	
	@PreAuthorize("hasAuthority('caixaREAD')")	
	@GetMapping(value="/balanco/hoje/{usuarioId}")
	public ResponseEntity<Object> geraBalancoCaixaHoje( @PathVariable Long usuarioId ) {
		try {						
			CaixaBalancoResponse resp = caixaService.geraCaixaBalancoHoje( usuarioId );
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
	
	@PreAuthorize("hasAuthority('caixaREAD')")
	@GetMapping(value="/get/uid/hoje/{usuarioId}")
	public ResponseEntity<Object> buscaPorUsuarioID( @PathVariable Long usuarioId ) {		
		try {
			CaixaResponse resp = caixaService.buscaCaixaHoje( usuarioId );
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
	
	@PreAuthorize("hasAuthority('caixaREAD')")
	@PostMapping(value="/lista/pordatadia")
	public ResponseEntity<Object> listaCaixasPorDataDia( @RequestBody BuscaCaixasPorDataDiaRequest request ) {
		try {
			List<CaixaResponse> caixas = caixaService.listaCaixasPorDataDia( request );
			return ResponseEntity.ok( caixas );
		} catch (DataDiaInvalidaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_DIA_INVALIDA ) );					
		}
	}
	
	@PreAuthorize("hasAuthority('caixaREAD')")
	@PostMapping(value="/gera/balancos/diarios")
	public ResponseEntity<Object> geraBalancosDiarios( @RequestBody BuscaBalancosDiarios request ) {
		if ( request.getDataIni() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_INI_OBRIGATORIA ) );		
		if ( request.getDataIni().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_INI_OBRIGATORIA ) );		
		
		if ( request.getDataFim() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_FIM_OBRIGATORIA ) );		
		if ( request.getDataFim().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_FIM_OBRIGATORIA ) );		
		
		try {
			List<BalancoDiarioResponse> balancos = caixaService.geraBalancosDiarios( request );
			return ResponseEntity.ok( balancos );
		} catch (DataIniInvalidaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_INI_INVALIDA ) );					
		} catch (DataFimInvalidaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_FIM_INVALIDA ) );					
		} catch (DataIniAposDataFimException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_INI_APOS_DATA_FIM ) );					
		}
	}
	
	@PreAuthorize("hasAuthority('caixaREAD')")
	@GetMapping(value="/get/{caixaId}")
	public ResponseEntity<Object> buscaCaixa( @PathVariable Long caixaId ) {
		try {
			CaixaResponse resp = caixaService.buscaCaixa( caixaId );
			return ResponseEntity.ok( resp );
		} catch ( CaixaNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CAIXA_NAO_ENCONTRADO ) );					
		}
	}
	
	@PreAuthorize("hasAuthority('caixaREAD')")
	@PostMapping(value="/filtra")
	public ResponseEntity<Object> filtra( @RequestBody BuscaCaixasRequest request ) {
		if ( request.getDataIni() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_INI_OBRIGATORIA ) );		
		if ( request.getDataIni().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_INI_OBRIGATORIA ) );		
		
		if ( request.getDataFim() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_FIM_OBRIGATORIA ) );		
		if ( request.getDataFim().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_FIM_OBRIGATORIA ) );		
		
		if ( request.getIncluirFuncionario() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FLAG_INCLUIR_FUNCIONARIO_OBRIGATORIO ) );		
		
		if ( request.getIncluirFuncionario().equals( "true" ) ) {
			if ( request.getFuncionarioNomeIni() == null )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FUNCIONARIO_NOME_OBRIGATORIO ) );		
			if ( request.getFuncionarioNomeIni().isBlank() )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FUNCIONARIO_NOME_OBRIGATORIO ) );					
		}
		
		try {
			List<CaixaResponse> lista = caixaService.filtra( request );
			return ResponseEntity.ok( lista );
		} catch (DataIniInvalidaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_INI_INVALIDA ) );					
		} catch (DataFimInvalidaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_FIM_INVALIDA ) );					
		} catch (DataIniAposDataFimException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_INI_APOS_DATA_FIM ) );					
		}
	}
		
	@PreAuthorize("hasAuthority('caixaDELETE')")
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
