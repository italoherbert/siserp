package italo.siserp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.siserp.exception.CaixaJaAbertoException;
import italo.siserp.exception.CaixaNaoAbertoException;
import italo.siserp.exception.CaixaValorInicialInvalidoException;
import italo.siserp.exception.PerfilCaixaRequeridoException;
import italo.siserp.model.request.AbreCaixaRequest;
import italo.siserp.model.response.CaixaResponse;
import italo.siserp.model.response.ErroResponse;
import italo.siserp.service.CaixaService;

@RestController
@RequestMapping(value="/api/caixa")
public class CaixaController {

	@Autowired
	private CaixaService caixaService;
	
	@PostMapping(value="/abrir/{usuarioId}")
	public ResponseEntity<Object> abrirCaixa( Long usuarioId, AbreCaixaRequest request ) {
		if ( request.getValorInicial() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CAIXA_VALOR_INICIAL_OBRIGATORIO ) );		
		if ( request.getValorInicial().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CAIXA_VALOR_INICIAL_OBRIGATORIO ) );		
			
		try {
			caixaService.abreGetCaixaSeNaoAberto( usuarioId, request );
			return ResponseEntity.ok().build();
		} catch (PerfilCaixaRequeridoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PERFIL_DE_CAIXA_REQUEERIDO ) );		
		} catch (CaixaValorInicialInvalidoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CAIXA_VALOR_INICIAL_INVALIDO ) );		
		} catch (CaixaJaAbertoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CAIXA_JA_ABERTO ) );					
		}		
	}
	
	@PostMapping(value="/get/uid/{usuarioId}")
	public ResponseEntity<Object> buscaPorUsuarioID( Long usuarioId ) {		
		try {
			CaixaResponse resp = caixaService.buscaPorUsuarioID( usuarioId );
			return ResponseEntity.ok( resp );
		} catch (PerfilCaixaRequeridoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PERFIL_DE_CAIXA_REQUEERIDO ) );		
		} catch (CaixaNaoAbertoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CAIXA_NAO_ABERTO ) );					
		}
	}
	
	@PostMapping(value="/get/fid/{funcionarioId}")
	public ResponseEntity<Object> buscaPorFuncionarioID( Long funcionarioId ) {		
		try {
			CaixaResponse resp = caixaService.buscaPorFuncionarioID( funcionarioId );
			return ResponseEntity.ok( resp );
		} catch (CaixaNaoAbertoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CAIXA_NAO_ABERTO ) );					
		} catch (PerfilCaixaRequeridoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PERFIL_DE_CAIXA_REQUEERIDO ) );		
		}
	}
		
}
