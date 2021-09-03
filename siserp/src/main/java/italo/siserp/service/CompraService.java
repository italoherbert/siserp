package italo.siserp.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.siserp.builder.CategoriaMapBuilder;
import italo.siserp.builder.CompraBuilder;
import italo.siserp.builder.CompraParcelaBuilder;
import italo.siserp.builder.FiltroCompraBuilder;
import italo.siserp.builder.FornecedorBuilder;
import italo.siserp.builder.ItemCompraBuilder;
import italo.siserp.builder.ProdutoBuilder;
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
import italo.siserp.model.CategoriaMap;
import italo.siserp.model.Compra;
import italo.siserp.model.CompraParcela;
import italo.siserp.model.Fornecedor;
import italo.siserp.model.ItemCompra;
import italo.siserp.model.Produto;
import italo.siserp.model.request.BuscaComprasRequest;
import italo.siserp.model.request.SaveCategoriaMapRequest;
import italo.siserp.model.request.SaveCompraParcelaRequest;
import italo.siserp.model.request.SaveCompraRequest;
import italo.siserp.model.request.SaveItemCompraRequest;
import italo.siserp.model.request.SaveProdutoRequest;
import italo.siserp.model.response.CompraResponse;
import italo.siserp.model.response.FiltroCompraResponse;
import italo.siserp.repository.CategoriaMapRepository;
import italo.siserp.repository.CompraRepository;
import italo.siserp.repository.FornecedorRepository;
import italo.siserp.repository.ProdutoRepository;
import italo.siserp.util.DataUtil;

@Service
public class CompraService {
		
	@Autowired
	private CompraRepository compraRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private FornecedorRepository fornecedorRepository;	
		
	@Autowired
	private CategoriaMapRepository categoriaMapRepository;
			
	@Autowired
	private CompraBuilder compraBuilder;

	@Autowired
	private FiltroCompraBuilder totalCompraBuilder;
	
	@Autowired
	private FornecedorBuilder fornecedorBuilder;
	
	@Autowired
	private CompraParcelaBuilder compraParcelaBuilder;
	
	@Autowired
	private ProdutoBuilder produtoBuilder;
		
	@Autowired
	private ItemCompraBuilder itemCompraBuilder;
	
	@Autowired
	private CategoriaMapBuilder categoriaMapBuilder;
			
	@Autowired
	private DataUtil dataUtil;
		
	public void salvaCompra( SaveCompraRequest request ) 
			throws DataCompraInvalidaException, 
				PrecoUnitCompraInvalidoException, 
				PrecoUnitVendaInvalidoException,
				QuantidadeInvalidaException,
				ParcelaValorInvalidoException, 
				DataPagamentoInvalidaException, 
				DataVencimentoInvalidaException { 
	
		List<CategoriaMap> cmapsParaRemover = new ArrayList<>();
		
		Compra compra = compraBuilder.novoCompra();
		compraBuilder.carregaCompra( compra, request );				
		
		List<ItemCompra> compraItens = new ArrayList<>();
				
		for( SaveItemCompraRequest icreq : request.getItensCompra() ) {
			ItemCompra ic = itemCompraBuilder.novoItemCompra();
			itemCompraBuilder.carregaItemCompra( ic, icreq );
								
			SaveProdutoRequest preq = icreq.getProduto();
			String codigoBarras = preq.getCodigoBarras();
			
			Optional<Produto> op = produtoRepository.findByCodigoBarras( codigoBarras );
			Produto p = null;
			if ( op.isPresent() ) {
				p = op.get();
				
				this.addProdutoQuantidade( p, preq );
				List<CategoriaMap> maps = p.getCategoriaMaps();
				for( CategoriaMap map : maps ) {
					map.setProduto( null );
					map.setCategoria( null );
					map.setSubcategoria( null );					
				}
				cmapsParaRemover.addAll( maps );
			} else {
				p = produtoBuilder.novoProduto();					
				produtoBuilder.carregaProduto( p, preq );
			}
			
			this.carregaCategorias( p, preq ); 			
																		
			ic.setProduto( p ); 
			ic.setCompra( compra );						
			
			compraItens.add( ic );									
		}
		compra.setItensCompra( compraItens ); 
		
		List<CompraParcela> parcelas = new ArrayList<>();
		for( SaveCompraParcelaRequest pReq : request.getParcelas() ) {
			CompraParcela parcela = new CompraParcela();
			compraParcelaBuilder.carregaCompraParcela( parcela, pReq );
			
			parcela.setCompra( compra );			
			parcelas.add( parcela );
		}
		compra.setParcelas( parcelas );						
		
		String femp = request.getFornecedor().getEmpresa();
		Optional<Fornecedor> fop = fornecedorRepository.buscaPorEmpresa( femp );
		Fornecedor f;
		if ( fop.isPresent() ) {
			f = fop.get();					
		} else {
			f = fornecedorBuilder.novoFornecedor();
		}
		fornecedorBuilder.carregaFornecedor( f, request.getFornecedor() ); 
						
		f.setCompras( Arrays.asList( compra ) );
		compra.setFornecedor( f ); 
		
		compraRepository.save( compra );
		for( CategoriaMap map : cmapsParaRemover )
			categoriaMapRepository.delete( map ); 
	}							
		
	private void carregaCategorias( Produto p, SaveProdutoRequest request ) {		
		if ( request.getCategoriaMaps() == null )
			return;					
		
		List<CategoriaMap> categoriasMap = new ArrayList<>();
		for( SaveCategoriaMapRequest mapreq : request.getCategoriaMaps() ) {
			Optional<CategoriaMap> mapOp = categoriaMapRepository.get( mapreq.getCategoria(), mapreq.getSubcategoria() );
			
			CategoriaMap map;
			
			if ( mapOp.isPresent() ) {
				map = mapOp.get();				
			} else {
				map = categoriaMapBuilder.novoCategoriaMap();
			}
			
			categoriaMapBuilder.carregaCategoriaMap( map, mapreq );
			
			map.setProduto( p ); 
			map.setCategoria( mapreq.getCategoria() );
			map.setSubcategoria( mapreq.getSubcategoria() );
				
			categoriasMap.add( map );																						
		}						
		
		p.setCategoriaMaps( categoriasMap );
	}
		
	private void addProdutoQuantidade( Produto p, SaveProdutoRequest request ) throws QuantidadeInvalidaException {
		try {
			p.setQuantidade( p.getQuantidade() + Double.parseDouble( request.getQuantidade() ) );
		} catch ( NumberFormatException e ) {
			QuantidadeInvalidaException ex = new QuantidadeInvalidaException();
			ex.setParams( request.getQuantidade() );
			throw ex;
		}
	}
	
	public List<FiltroCompraResponse> filtra( BuscaComprasRequest request ) 
			throws DataIniInvalidaException, 
					DataFimInvalidaException, 
					DataIniAposDataFimException {
		Date dataIni, dataFim;
		try {
			dataIni = dataUtil.stringParaData( request.getDataIni() );
		} catch ( ParseException e ) {
			DataIniInvalidaException ex = new DataIniInvalidaException();
			ex.setParams( request.getDataIni() ); 
			throw ex;
		}
		try {
			dataFim = dataUtil.stringParaData( request.getDataFim() );
		} catch ( ParseException e ) {
			DataFimInvalidaException ex = new DataFimInvalidaException();
			ex.setParams( request.getDataFim() ); 
			throw ex;
		}
		
		if ( dataIni.after( dataFim ) )
			throw new DataIniAposDataFimException();
		
		List<Compra> compras = compraRepository.filtra( dataIni, dataFim );
		List<FiltroCompraResponse> responses = new ArrayList<>();
		for( Compra c : compras ) {
			FiltroCompraResponse resp = totalCompraBuilder.novoTotalCompraResponse();
			totalCompraBuilder.carregaTotalCompraResponse( resp, c );
			
			responses.add( resp );
		}
		
		return responses;
	}
	
	public CompraResponse buscaCompra( Long id ) throws CompraNaoEncontradaException {
		Compra c = compraRepository.findById( id ).orElseThrow( CompraNaoEncontradaException::new );
		
		CompraResponse resp = compraBuilder.novoCompraResponse();
		compraBuilder.carregaCompraResponse( resp, c );
		
		return resp;
	}
	
	public void delete( Long id ) throws CompraNaoEncontradaException {
		Compra c = compraRepository.findById( id ).orElseThrow( CompraNaoEncontradaException::new );
		List<ItemCompra> itens = c.getItensCompra();
		for( ItemCompra ic : itens ) {
			Produto p = ic.getProduto();
			if ( ic.getQuantidade() > p.getQuantidade() ) {
				p.setQuantidade( 0 );
			} else {
				p.setQuantidade( p.getQuantidade() - ic.getQuantidade() );
			}
			produtoRepository.save( p );
		}
		
		compraRepository.delete( c );
	}
	
}