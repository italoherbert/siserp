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

import italo.siserp.exception.CompraNaoEncontradaException;
import italo.siserp.exception.DataCompraInvalidaException;
import italo.siserp.exception.DataFimInvalidaException;
import italo.siserp.exception.DataIniAposDataFimException;
import italo.siserp.exception.DataIniInvalidaException;
import italo.siserp.exception.DataPagamentoInvalidaException;
import italo.siserp.exception.DataVencimentoInvalidaException;
import italo.siserp.exception.ParcelaValorInvalidoException;
import italo.siserp.exception.PrecoUnitCompraInvalidoException;
import italo.siserp.exception.PrecoUnitVendaInvalidoException;
import italo.siserp.exception.QuantidadeInvalidaException;
import italo.siserp.service.CompraService;
import italo.siserp.service.request.BuscaComprasRequest;
import italo.siserp.service.request.SaveCategoriaMapRequest;
import italo.siserp.service.request.SaveCompraParcelaRequest;
import italo.siserp.service.request.SaveCompraRequest;
import italo.siserp.service.request.SaveItemCompraRequest;
import italo.siserp.service.response.CompraResponse;
import italo.siserp.service.response.ErroResponse;
import italo.siserp.service.response.FiltroCompraResponse;

@RestController
@RequestMapping(value = "/api/compra")
public class CompraController {

	@Autowired
	private CompraService compraService;
	
	@PreAuthorize("hasAuthority('compraWRITE')")	
	@PostMapping(value="/registra")
	public ResponseEntity<Object> registraCompra( @RequestBody SaveCompraRequest request ) {
		if ( request.getDataCompra() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_COMPRA_INVALIDA ) );						
		if ( request.getDataCompra().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_COMPRA_INVALIDA ) );
		
		if ( request.getFornecedor().getEmpresa() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FORNECEDOR_NAO_ENCONTRADO ) );						
		if ( request.getFornecedor().getEmpresa().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FORNECEDOR_NAO_ENCONTRADO ) );

		if ( request.getItensCompra().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.NENHUM_PRODUTO_INFORMADO ) );						
				
		for( SaveItemCompraRequest icreq : request.getItensCompra() ) {			
			if ( icreq.getPrecoUnitario() == null )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_PRECO_UNIT_COMPRA_OBRIGATORIO ) );								
			if ( icreq.getPrecoUnitario().isBlank() )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_PRECO_UNIT_COMPRA_OBRIGATORIO ) );
			
			if ( icreq.getQuantidade() == null )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_QUANTIDADE_OBRIGATORIA ) );								
			if ( icreq.getQuantidade().isBlank() )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_QUANTIDADE_OBRIGATORIA ) );
			
			if ( icreq.getProduto().getDescricao() == null )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_DESCRICAO_OBRIGATORIA ) );								
			if ( icreq.getProduto().getDescricao().isBlank() )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_DESCRICAO_OBRIGATORIA ) );
			
			if ( icreq.getProduto().getPrecoUnitCompra() == null )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_PRECO_UNIT_COMPRA_OBRIGATORIO ) );								
			if ( icreq.getProduto().getPrecoUnitCompra().isBlank() )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_PRECO_UNIT_COMPRA_OBRIGATORIO ) );
			
			if ( icreq.getProduto().getPrecoUnitVenda() == null )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_PRECO_UNIT_VENDA_OBRIGATORIO ) );								
			if ( icreq.getProduto().getPrecoUnitVenda().isBlank() )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_PRECO_UNIT_VENDA_OBRIGATORIO ) );
			
			if ( icreq.getProduto().getUnidade() == null )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_UNIDADE_OBRIGATORIA ) );								
			
			if ( icreq.getProduto().getQuantidade() == null )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_QUANTIDADE_OBRIGATORIA ) );								
			if ( icreq.getProduto().getQuantidade().isBlank() )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_QUANTIDADE_OBRIGATORIA ) );
			
			if ( icreq.getProduto().getCodigoBarras() == null )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_CODIGO_BARRAS_OBRIGATORIO ) );								
			if ( icreq.getProduto().getCodigoBarras().isBlank() )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_CODIGO_BARRAS_OBRIGATORIO ) );			
			
			for ( SaveCategoriaMapRequest mapreq : icreq.getProduto().getCategoriaMaps() ) {
				if ( mapreq.getCategoria() == null )
					return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CATEGORIA_DESCRICAO_OBRIGATORIA ) );
				if ( mapreq.getCategoria().isBlank() )
					return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CATEGORIA_DESCRICAO_OBRIGATORIA ) );
				
				if ( mapreq.getSubcategoria() == null )
					return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.SUBCATEGORIA_DESCRICAO_OBRIGATORIA ) );
				if ( mapreq.getSubcategoria().trim().isEmpty() )
					return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.SUBCATEGORIA_DESCRICAO_OBRIGATORIA ) );									
			}			
		}
		
		for( SaveCompraParcelaRequest parcelaReq : request.getParcelas() ) {
			if ( parcelaReq.getValor() == null )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PARCELA_VALOR_OBRIGATORIO ) );
			if ( parcelaReq.getValor() == null )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PARCELA_VALOR_OBRIGATORIO ) );		
		
			if ( parcelaReq.getDataPagamento() == null )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_PAGAMENTO_OBRIGATORIA ) );
			if ( parcelaReq.getDataPagamento() == null )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_PAGAMENTO_OBRIGATORIA ) );
			
			if ( parcelaReq.getDataVencimento() == null )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_VENCIMENTO_OBRIGATORIA ) );
			if ( parcelaReq.getDataVencimento() == null )
				return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_VENCIMENTO_OBRIGATORIA ) );										
		}
		
		try {
			compraService.salvaCompra( request );
			return ResponseEntity.ok().build();
		} catch (DataCompraInvalidaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_COMPRA_INVALIDA, e.getParams() ) );
		} catch (PrecoUnitCompraInvalidoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_PRECO_UNIT_COMPRA_INVALIDO, e.getParams() ) );			
		} catch (PrecoUnitVendaInvalidoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_PRECO_UNIT_VENDA_INVALIDO, e.getParams() ) );			
		} catch (QuantidadeInvalidaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.QUANTIDADE_INVALIDA, e.getParams() ) );			
		} catch (ParcelaValorInvalidoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PARCELA_VALOR_INVALIDO, e.getParams() ) );			
		} catch (DataPagamentoInvalidaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_PAGAMENTO_INVALIDA, e.getParams() ) );						
		} catch (DataVencimentoInvalidaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_VENCIMENTO_INVALIDA, e.getParams() ) );						
		}
	}
	
	@PreAuthorize("hasAuthority('compraREAD')")	
	@PostMapping(value="/filtra")
	public ResponseEntity<Object> filtraCompras( @RequestBody BuscaComprasRequest request ) {
		if ( request.getDataIni() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_INI_OBRIGATORIA ) );
		if ( request.getDataIni().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_INI_OBRIGATORIA ) );
		if ( request.getDataFim() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_FIM_OBRIGATORIA ) );
		if ( request.getDataFim().isBlank() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_FIM_OBRIGATORIA ) );
		
		try {
			List<FiltroCompraResponse> resps = compraService.filtra( request );
			return ResponseEntity.ok( resps );
		} catch (DataIniInvalidaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_INI_INVALIDA ) );						
		} catch (DataFimInvalidaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_FIM_INVALIDA ) );						
		} catch (DataIniAposDataFimException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_INI_APOS_DATA_FIM ) );						
		}
	}
	
	@PreAuthorize("hasAuthority('compraREAD')")	
	@GetMapping(value="/get/{id}")
	public ResponseEntity<Object> buscaCompra( @PathVariable Long id ) {
		try {
			CompraResponse resp = compraService.buscaCompra( id );
			return ResponseEntity.ok( resp );
		} catch (CompraNaoEncontradaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.COMPRA_NAO_ENCONTRADA ) );						
		}
	}
	
	@PreAuthorize("hasAuthority('compraDELETE')")	
	@DeleteMapping(value="/deleta/{id}")
	public ResponseEntity<Object> deletaCompra( @PathVariable Long id ) {
		try {
			compraService.delete( id );
			return ResponseEntity.ok().build();
		} catch (CompraNaoEncontradaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.COMPRA_NAO_ENCONTRADA ) );						
		}
	}
		
}
