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
import italo.siserp.exception.ClienteNaoEncontradoException;
import italo.siserp.exception.DataFimAposDataIniException;
import italo.siserp.exception.DataFimInvalidaException;
import italo.siserp.exception.DataIniInvalidaException;
import italo.siserp.exception.DataVendaInvalidaException;
import italo.siserp.exception.DebitoInvalidoException;
import italo.siserp.exception.DescontoInvalidoException;
import italo.siserp.exception.PerfilCaixaRequeridoException;
import italo.siserp.exception.PrecoUnitVendaInvalidoException;
import italo.siserp.exception.ProdutoNaoEncontradoException;
import italo.siserp.exception.QuantidadeInvalidaException;
import italo.siserp.exception.SubtotalInvalidoException;
import italo.siserp.exception.ValorPagoInvalidoException;
import italo.siserp.exception.VendaNaoEncontradaException;
import italo.siserp.model.Caixa;
import italo.siserp.model.request.BuscaVendasRequest;
import italo.siserp.model.request.EfetuarPagamentoRequest;
import italo.siserp.model.request.SaveItemVendaRequest;
import italo.siserp.model.request.SaveVendaRequest;
import italo.siserp.model.response.EfetuarVendaPagamentoResponse;
import italo.siserp.model.response.ErroResponse;
import italo.siserp.model.response.QuitarDebitoResponse;
import italo.siserp.model.response.VendaResponse;
import italo.siserp.service.CaixaService;
import italo.siserp.service.VendaService;

@RestController
@RequestMapping(value = "/api/venda")
public class VendaController {

	@Autowired
	private VendaService vendaService;
	
	@Autowired
	private CaixaService caixaService;
	
	@PostMapping(value="/efetua/{usuarioId}")
	public ResponseEntity<Object> efetuaVenda( @PathVariable Long usuarioId, @RequestBody SaveVendaRequest request ) {
		if ( request.getDataVenda() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_VENDA_INVALIDA ) );						
		if ( request.getDataVenda().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_VENDA_INVALIDA ) );
		
		if ( request.getIncluirCliente() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.INCLUIR_CLIENTE_VALOR_INVALIDO ) );						
		if ( request.getIncluirCliente().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.INCLUIR_CLIENTE_VALOR_INVALIDO ) );

		if ( request.getIncluirCliente().equals( "true" ) ) {
			if ( request.getClienteId() == null )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CLIENTE_NAO_ENCONTRADO ) );
			if ( request.getClienteId().trim().isEmpty() )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CLIENTE_NAO_ENCONTRADO ) );
		}
				
		for( SaveItemVendaRequest icreq : request.getItensVenda() ) {						
			if ( icreq.getQuantidade() == null )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_QUANTIDADE_OBRIGATORIA ) );								
			if ( icreq.getQuantidade().trim().isEmpty() )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_QUANTIDADE_OBRIGATORIA ) );												
		}
				
		try {
			Caixa c = caixaService.buscaHojeCaixa( usuarioId );
			EfetuarVendaPagamentoResponse resp = vendaService.efetuaVenda( c, request );
			return ResponseEntity.ok( resp );
		} catch (DataVendaInvalidaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_COMPRA_INVALIDA, e.getParams() ) );
		} catch (PrecoUnitVendaInvalidoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_PRECO_UNIT_COMPRA_INVALIDO, e.getParams() ) );			
		} catch (QuantidadeInvalidaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.QUANTIDADE_INVALIDA, e.getParams() ) );			
		} catch (SubtotalInvalidoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.SUBTOTAL_INVALIDO, e.getParams() ) );			
		} catch (DescontoInvalidoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DESCONTO_INVALIDO, e.getParams() ) );			
		} catch (DebitoInvalidoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DEBITO_INVALIDO, e.getParams() ) );			
		} catch (ProdutoNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_NAO_ENCONTRADO ) );			
		} catch (PerfilCaixaRequeridoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PERFIL_DE_CAIXA_REQUEERIDO ) );			
		} catch (CaixaNaoAbertoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CAIXA_NAO_ABERTO) );						
		} catch (ValorPagoInvalidoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.VALOR_PAGO_INVALIDO ) );						
		} catch (ClienteNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CLIENTE_NAO_ENCONTRADO ) );						
		}
	}
	
	@PostMapping(value="/filtra")
	public ResponseEntity<Object> filtraVendas( @RequestBody BuscaVendasRequest request ) {
		if ( request.getIncluirCliente() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FLAG_INCLUIR_CLIENTE_OBRIGATORIO ) );
		if ( request.getIncluirCliente().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FLAG_INCLUIR_CLIENTE_OBRIGATORIO ) );
		
		if ( request.getIncluirCliente().equals( "true") ) {
			if ( request.getClienteNomeIni() == null )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CLIENTE_NOME_OBRIGATORIO ) );
			if ( request.getClienteNomeIni().trim().isEmpty() )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CLIENTE_NOME_OBRIGATORIO ) );			
		}
		
		try {
			List<VendaResponse> resps = vendaService.filtra( request );
			return ResponseEntity.ok( resps );
		} catch (DataIniInvalidaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_INI_INVALIDA, e.getParams() ) );						
		} catch (DataFimInvalidaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_FIM_INVALIDA, e.getParams() ) );						
		} catch (DataFimAposDataIniException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_INI_APOS_DATA_FIM ) );						
		}
	}
	
	@GetMapping(value="/get/{id}")
	public ResponseEntity<Object> buscaVenda( @PathVariable Long id ) {
		try {
			VendaResponse resp = vendaService.buscaVendaPorId( id );
			return ResponseEntity.ok( resp );
		} catch (VendaNaoEncontradaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.COMPRA_NAO_ENCONTRADA ) );						
		}
	}
	
	@DeleteMapping(value="/deleta/{id}")
	public ResponseEntity<Object> deletaVenda( @PathVariable Long id ) {
		try {
			vendaService.deleta( id );
			return ResponseEntity.ok().build();
		} catch (VendaNaoEncontradaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.COMPRA_NAO_ENCONTRADA ) );						
		}
	}
		
	@PostMapping(value="/efetuapag/{clienteId}")
	public ResponseEntity<Object> efetuaPagamento( @PathVariable Long clienteId, EfetuarPagamentoRequest request ) {
		try {
			QuitarDebitoResponse resp = vendaService.efetuarPagamento( clienteId, request );
			return ResponseEntity.ok( resp );
		} catch (ClienteNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CLIENTE_NAO_ENCONTRADO ) );						
		} catch (ValorPagoInvalidoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.VALOR_PAGO_INVALIDO ) );						
		}
	}
	
}

