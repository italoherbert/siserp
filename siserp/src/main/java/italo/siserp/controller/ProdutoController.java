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

import italo.siserp.exception.PrecoUnitCompraInvalidoException;
import italo.siserp.exception.PrecoUnitVendaInvalidoException;
import italo.siserp.exception.ProdutoJaExisteException;
import italo.siserp.exception.ProdutoNaoEncontradoException;
import italo.siserp.exception.QuantidadeInvalidaException;
import italo.siserp.model.request.SaveProdutoRequest;
import italo.siserp.model.response.ErroResponse;
import italo.siserp.model.response.ProdutoResponse;
import italo.siserp.service.ProdutoService;

@RestController
@RequestMapping(value="/api/produto")
public class ProdutoController {
	
	@Autowired
	private ProdutoService produtoService;
	
	@PreAuthorize("hasAnyAuthority('ADMIN', 'GERENTE')")
	@PostMapping("/salva")
	public ResponseEntity<Object> registra( @RequestBody SaveProdutoRequest request ) {
		if ( request.getQuantidade() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_QUANTIDADE_OBRIGATORIA ) );		
		if ( request.getQuantidade().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_QUANTIDADE_OBRIGATORIA ) );		
		if ( request.getCodigoBarras() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_CODIGO_BARRAS_OBRIGATORIO ) );		
		if ( request.getCodigoBarras().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_CODIGO_BARRAS_OBRIGATORIO ) );		
	
		
		if ( request.getDescricao() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_DESCRICAO_OBRIGATORIA ) );		
		if ( request.getDescricao().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_DESCRICAO_OBRIGATORIA ) );		
		if ( request.getUnidade() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_UNIDADE_OBRIGATORIA ) );		
		if ( request.getUnidade().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_UNIDADE_OBRIGATORIA ) );		
		if ( request.getPrecoUnitCompra() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_PRECO_UNIT_COMPRA_OBRIGATORIO ) );		
		if ( request.getPrecoUnitCompra().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_PRECO_UNIT_COMPRA_OBRIGATORIO ) );		
		if ( request.getPrecoUnitVenda() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_PRECO_UNIT_VENDA_OBRIGATORIO ) );		
		if ( request.getPrecoUnitVenda().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_PRECO_UNIT_VENDA_OBRIGATORIO ) );		
				
		try {
			produtoService.salvaProduto( request );
			return ResponseEntity.ok().build();
		} catch (ProdutoJaExisteException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_JA_EXISTE ) );		
		} catch (PrecoUnitCompraInvalidoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_PRECO_UNIT_COMPRA_INVALIDO ) );		
		} catch (PrecoUnitVendaInvalidoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_PRECO_UNIT_VENDA_INVALIDO ) );		
		} catch (QuantidadeInvalidaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.QUANTIDADE_INVALIDA ) );		
		}				
	}		

	@PreAuthorize("hasAnyAuthority('ADMIN', 'GERENTE', 'CAIXA')")
	@GetMapping("/filtra/{descricaoIni}")
	public ResponseEntity<Object> filtraPorDescIni( @PathVariable String descricaoIni ) {
		if ( descricaoIni == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_DESCRICAO_OBRIGATORIA ) );		
		if ( descricaoIni.trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_DESCRICAO_OBRIGATORIA ) );		
				
		List<ProdutoResponse> produtos = produtoService.buscaProdutosPorDescIni( descricaoIni );
		return ResponseEntity.ok( produtos );		
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN', 'GERENTE', 'CAIXA')")
	@GetMapping("/busca/{codigoBarras}")
	public ResponseEntity<Object> filtraPorCodBarras( @PathVariable String codigoBarras ) {
		if ( codigoBarras == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_CODIGO_BARRAS_OBRIGATORIO ) );		
		if ( codigoBarras.trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_CODIGO_BARRAS_OBRIGATORIO ) );		
				
		try {
			ProdutoResponse produto = produtoService.buscaProdutoPorCodBarra( codigoBarras );
			return ResponseEntity.ok( produto );		
		} catch (ProdutoNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_NAO_ENCONTRADO ) );		
		}
	}

	@PreAuthorize("hasAnyAuthority('ADMIN', 'GERENTE', 'CAIXA')")	
	@GetMapping("/get/{id}")
	public ResponseEntity<Object> buscaProdutoPorId( @PathVariable Long id ) {		
		try {
			ProdutoResponse produto = produtoService.buscaProdutoPorId( id );
			return ResponseEntity.ok( produto );
		} catch (ProdutoNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_NAO_ENCONTRADO ) );		
		}
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN', 'GERENTE')")
	@DeleteMapping("/deleta/{id}")
	public ResponseEntity<Object> deletaProduto( @PathVariable Long id ) {		
		try {
			produtoService.deleta( id );
			return ResponseEntity.ok().build();
		} catch (ProdutoNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_NAO_ENCONTRADO ) );		
		}
	}
	
}

