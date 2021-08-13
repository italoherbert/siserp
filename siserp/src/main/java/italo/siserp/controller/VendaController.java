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

import italo.siserp.exception.CaixaNaoAbertoException;
import italo.siserp.exception.ClienteNaoEncontradoException;
import italo.siserp.exception.DataFimAposDataIniException;
import italo.siserp.exception.DataFimInvalidaException;
import italo.siserp.exception.DataIniInvalidaException;
import italo.siserp.exception.DataVendaInvalidaException;
import italo.siserp.exception.DebitoInvalidoException;
import italo.siserp.exception.DescontoInvalidoException;
import italo.siserp.exception.FormaPagInvalidaException;
import italo.siserp.exception.FuncionarioNaoEncontradoException;
import italo.siserp.exception.PerfilCaixaRequeridoException;
import italo.siserp.exception.PrecoUnitVendaInvalidoException;
import italo.siserp.exception.ProdutoNaoEncontradoException;
import italo.siserp.exception.QuantidadeInvalidaException;
import italo.siserp.exception.SubtotalInvalidoException;
import italo.siserp.exception.UsuarioNaoEncontradoException;
import italo.siserp.exception.ValorPagoInvalidoException;
import italo.siserp.exception.ValorRecebidoInvalidoException;
import italo.siserp.exception.VendaNaoEncontradaException;
import italo.siserp.model.FormaPag;
import italo.siserp.model.request.BuscaVendasRequest;
import italo.siserp.model.request.SaveItemVendaRequest;
import italo.siserp.model.request.SaveVendaRequest;
import italo.siserp.model.request.ValorRecebidoRequest;
import italo.siserp.model.response.ErroResponse;
import italo.siserp.model.response.VendaResponse;
import italo.siserp.service.VendaService;
import italo.siserp.util.FormaPagEnumConversor;

@RestController
@RequestMapping(value = "/api/venda")
public class VendaController {

	@Autowired
	private VendaService vendaService;
	
	@Autowired
	private FormaPagEnumConversor formaPagEnumConversor;
		
	@PreAuthorize("hasAnyAuthority('vendaWRITE')")	
	@PostMapping(value="/efetua/{usuarioId}")
	public ResponseEntity<Object> efetuaVenda( @PathVariable Long usuarioId, @RequestBody SaveVendaRequest request ) {		
		if ( request.getIncluirCliente() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FLAG_INCLUIR_CLIENTE_VALOR_INVALIDO ) );						
		if ( request.getIncluirCliente().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FLAG_INCLUIR_CLIENTE_VALOR_INVALIDO ) );

		boolean incluirCliente = request.getIncluirCliente().equals( "true" );
		FormaPag formaPag = formaPagEnumConversor.getFormaPag( request.getFormaPag() );
		
		if ( incluirCliente || formaPag == FormaPag.DEBITO ) {
			if ( request.getClienteNome() == null )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CLIENTE_NOME_OBRIGATORIO ) );
			if ( request.getClienteNome().isBlank() )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CLIENTE_NOME_OBRIGATORIO ) );
		}
				
		for( SaveItemVendaRequest icreq : request.getItensVenda() ) {						
			if ( icreq.getQuantidade() == null )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_QUANTIDADE_OBRIGATORIA ) );								
			if ( icreq.getQuantidade().isBlank() )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_QUANTIDADE_OBRIGATORIA ) );												
		}
				
		try {
			vendaService.efetuaVenda( usuarioId, request );
			return ResponseEntity.ok().build();
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
		} catch (UsuarioNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_NAO_ENCONTRADO ) );					
		} catch (FuncionarioNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FUNCIONARIO_NAO_ENCONTRADO ) );					
		} catch (CaixaNaoAbertoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CAIXA_NAO_ABERTO) );						
		} catch (ValorPagoInvalidoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.VALOR_PAGO_INVALIDO ) );						
		} catch (ClienteNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CLIENTE_NAO_ENCONTRADO ) );						
		} catch (FormaPagInvalidaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FORMA_PAG_INVALIDA ) );						
		}
	}
	
	@PreAuthorize("hasAnyAuthority('vendaREAD')")	
	@PostMapping(value="/filtra")
	public ResponseEntity<Object> filtraVendas( @RequestBody BuscaVendasRequest request ) {
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
		
		if ( request.getIncluirPagas() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FLAG_INCLUIR_VENDAS_PAGAS_OBRIGATORIO ) );		
		
		if ( request.getIncluirCliente().equals( "true") ) {
			if ( request.getClienteNomeIni() == null )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CLIENTE_NOME_OBRIGATORIO ) );
			if ( request.getClienteNomeIni().isBlank() )
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
	
	@PreAuthorize("hasAnyAuthority('vendaREAD')")	
	@GetMapping("/lista/porcliente/{clienteId}")
	public ResponseEntity<Object> buscaVendasPorClienteId( @PathVariable Long clienteId ) {
		List<VendaResponse> lista = vendaService.buscaVendasPorClienteId( clienteId );
		return ResponseEntity.ok( lista ); 
	}
	
	@PreAuthorize("hasAnyAuthority('vendaREAD')")	
	@GetMapping(value="/get/{id}")
	public ResponseEntity<Object> buscaVenda( @PathVariable Long id ) {
		try {
			VendaResponse resp = vendaService.buscaVendaPorId( id );
			return ResponseEntity.ok( resp );
		} catch (VendaNaoEncontradaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.COMPRA_NAO_ENCONTRADA ) );						
		}
	}
	
	@PreAuthorize("hasAnyAuthority('vendaDELETE')")	
	@DeleteMapping(value="/deleta/{id}")
	public ResponseEntity<Object> deletaVenda( @PathVariable Long id ) {
		try {
			vendaService.deleta( id );
			return ResponseEntity.ok().build();
		} catch (VendaNaoEncontradaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.COMPRA_NAO_ENCONTRADA ) );						
		}
	}
		 
	@PreAuthorize("hasAnyAuthority('vendaWRITE')")	
	@PostMapping(value="/efetuarecebimento/{clienteId}")
	public ResponseEntity<Object> efetuaPagamento( @PathVariable Long clienteId, @RequestBody ValorRecebidoRequest request ) {
		if ( request.getValor() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.VALOR_RECEBIDO_INVALIDO ) );						
		if ( request.getValor().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.VALOR_RECEBIDO_INVALIDO ) );						
			
		try {			
			vendaService.efetuaRecebimento( clienteId, request );			
			return ResponseEntity.ok().build();
		} catch (ClienteNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CLIENTE_NAO_ENCONTRADO ) );						
		} catch (ValorRecebidoInvalidoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.VALOR_RECEBIDO_INVALIDO ) );						
		}
	}
	
}

