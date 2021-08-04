package italo.siserp.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.siserp.builder.CategoriaBuilder;
import italo.siserp.builder.CompraBuilder;
import italo.siserp.builder.CompraParcelaBuilder;
import italo.siserp.builder.FornecedorBuilder;
import italo.siserp.builder.ItemCompraBuilder;
import italo.siserp.builder.ItemProdutoBuilder;
import italo.siserp.builder.ProdutoBuilder;
import italo.siserp.builder.SubCategoriaBuilder;
import italo.siserp.builder.TotalCompraBuilder;
import italo.siserp.exception.CompraNaoEncontradaException;
import italo.siserp.exception.DataCompraException;
import italo.siserp.exception.DataFimAposDataIniException;
import italo.siserp.exception.DataFimInvalidaException;
import italo.siserp.exception.DataIniInvalidaException;
import italo.siserp.exception.DataPagamentoInvalidaException;
import italo.siserp.exception.DataVencimentoInvalidaException;
import italo.siserp.exception.ParcelaValorInvalidoException;
import italo.siserp.exception.PrecoUnitCompraInvalidoException;
import italo.siserp.exception.PrecoUnitVendaInvalidoException;
import italo.siserp.exception.QuantidadeInvalidaException;
import italo.siserp.model.Categoria;
import italo.siserp.model.CategoriaMap;
import italo.siserp.model.Compra;
import italo.siserp.model.CompraParcela;
import italo.siserp.model.Fornecedor;
import italo.siserp.model.ItemCompra;
import italo.siserp.model.ItemProduto;
import italo.siserp.model.Produto;
import italo.siserp.model.SubCategoria;
import italo.siserp.model.request.BuscaCompraRequest;
import italo.siserp.model.request.SaveCategoriaRequest;
import italo.siserp.model.request.SaveCompraParcelaRequest;
import italo.siserp.model.request.SaveCompraRequest;
import italo.siserp.model.request.SaveItemCompraRequest;
import italo.siserp.model.request.SaveItemProdutoRequest;
import italo.siserp.model.request.SaveSubCategoriaRequest;
import italo.siserp.model.response.CompraResponse;
import italo.siserp.model.response.TotalCompraResponse;
import italo.siserp.repository.CategoriaMapRepository;
import italo.siserp.repository.CategoriaRepository;
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
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private CategoriaMapRepository categoriaMapRepository;	
		
		
	@Autowired
	private CompraBuilder compraBuilder;

	@Autowired
	private TotalCompraBuilder totalCompraBuilder;
	
	@Autowired
	private FornecedorBuilder fornecedorBuilder;
	
	@Autowired
	private CompraParcelaBuilder compraParcelaBuilder;
	
	@Autowired
	private ProdutoBuilder produtoBuilder;
	
	@Autowired
	private ItemProdutoBuilder itemProdutoBuilder;
	
	@Autowired
	private ItemCompraBuilder itemCompraBuilder;
	
	@Autowired
	private CategoriaBuilder categoriaBuilder;
		
	@Autowired
	private SubCategoriaBuilder subcategoriaBuilder;		
	
	@Autowired
	private DataUtil dataUtil;
		
	public void salvaCompra( SaveCompraRequest request ) 
			throws DataCompraException, 
				PrecoUnitCompraInvalidoException, 
				PrecoUnitVendaInvalidoException,
				QuantidadeInvalidaException,
				ParcelaValorInvalidoException, 
				DataPagamentoInvalidaException, 
				DataVencimentoInvalidaException { 
	
		Compra compra = compraBuilder.novoCompra();
		compraBuilder.carregaCompra( compra, request );
		
		List<ItemCompra> compraItens = new ArrayList<>();
		
		List<Produto> itensProdutosList = new ArrayList<>();
		
		for( SaveItemCompraRequest icreq : request.getItensCompra() ) {
			ItemCompra ic = itemCompraBuilder.novoItemCompra();
			itemCompraBuilder.carregaItemCompra( ic, icreq );
								
			String codigoBarras = icreq.getProduto().getCodigoBarras();
			Optional<Produto> pop = produtoRepository.findByCodigoBarras( codigoBarras );
			Produto p;
			if ( pop.isPresent() ) {
				p = pop.get();
			} else {
				p = null;
				int size = itensProdutosList.size();
				for( int i = 0; p == null && i < size; i++ ) {
					Produto p2 = itensProdutosList.get( i );
					if ( p2.getCodigoBarras().equalsIgnoreCase( codigoBarras ) )
						p = p2;						
				}
				if ( p == null ) {
					p = produtoBuilder.novoProduto();
					itensProdutosList.add( p );
				}
			}
			
			produtoBuilder.carregaProduto( p, icreq.getProduto() );
			
			SaveItemProdutoRequest ipreq = icreq.getItemProduto();
			ItemProduto ip = this.buscaItemProduto( codigoBarras , ipreq.getCategorias() );
			if ( ip == null ) {		
				ip = itemProdutoBuilder.novoItemProduto();
				
				itemProdutoBuilder.carregaItemProduto( ip, ipreq );
				this.carregaCategorias( ip, ipreq ); 
			} else {
				this.addItemProdutoQuantidade( ip, ipreq ); 
			}
			ip.setProduto( p ); 
			p.getItensProdutos().add( ip );
									
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
	}
			
				
	private ItemProduto buscaItemProduto( String codigoBarras, List<SaveCategoriaRequest> categorias ) {		
		Optional<Produto> op = produtoRepository.findByCodigoBarras( codigoBarras );
		if ( op.isPresent() ) {
			Produto produto = op.get();
			if( categorias == null ) {
				if ( produto.getItensProdutos().isEmpty() )
					return null;
				
				return produto.getItensProdutos().get( 0 );
			}				
			
			if ( categorias.isEmpty() ) {
				if ( produto.getItensProdutos().isEmpty() )
					return null;
				
				return produto.getItensProdutos().get( 0 );
			} else {
				for( ItemProduto ip : produto.getItensProdutos() ) {					
					boolean existe = true;					
					int csize = categorias.size();
					for( int i = 0; existe && i < csize; i++ ) {
						SaveCategoriaRequest creq = categorias.get( i );
						String categ = creq.getDescricao();
						
						int scsize = creq.getSubcategorias().size();
						for( int j = 0; existe && j < scsize; j++ ) {
							SaveSubCategoriaRequest screq = creq.getSubcategorias().get( j );
							String subcateg = screq.getDescricao();
							
							Optional<CategoriaMap> map = categoriaMapRepository.temCategoria( codigoBarras, categ, subcateg );
							existe = map.isPresent();															
						}
					}		
					
					if ( existe )
						return ip;					
				}
			}
		}			
		
		return null;
	}
	
	private void carregaCategorias( ItemProduto ip, SaveItemProdutoRequest request ) {						
		if ( request.getCategorias() == null )
			return;

		List<CategoriaMap> categoriasMap = new ArrayList<>();
		for( SaveCategoriaRequest catreq : request.getCategorias() ) {
			Categoria c;
			Optional<Categoria> opc = categoriaRepository.buscaPorDescricao( catreq.getDescricao() );
			if ( opc.isPresent() ) {
				c = opc.get();
			} else {
				c = categoriaBuilder.novoCategoria();
			}
							
			categoriaBuilder.carregaCategoria( c, catreq );
							
			for( SaveSubCategoriaRequest subcatreq : catreq.getSubcategorias() ) {
				SubCategoria sc = null;
				if ( opc.isPresent() ) {
					int subcategoriasSize = c.getSubcategorias().size();
					for( int i = 0; sc == null && i < subcategoriasSize; i++ ) {
						SubCategoria sc2 = c.getSubcategorias().get( i ); 
						if ( sc2.getDescricao().equalsIgnoreCase( subcatreq.getDescricao() ) )
							sc = sc2;
					}
				}
				
				if ( sc == null )
					sc = subcategoriaBuilder.novoSubCategoria();
				
				subcategoriaBuilder.carregaSubCategoria( sc, subcatreq );
				
				CategoriaMap map = new CategoriaMap();
				map.setItemProduto( ip ); 
				map.setCategoria( c );
				map.setSubcategoria( sc );
				
				sc.setCategoria( c ); 
				
				categoriasMap.add( map );
			}
		}						
		
		ip.setCategoriaMaps( categoriasMap );
	}
	
	private void addItemProdutoQuantidade( ItemProduto ip, SaveItemProdutoRequest request ) throws QuantidadeInvalidaException {
		try {
			ip.setQuantidade( ip.getQuantidade() + Double.parseDouble( request.getQuantidade() ) );
		} catch ( NumberFormatException e ) {
			QuantidadeInvalidaException ex = new QuantidadeInvalidaException();
			ex.setParams( request.getQuantidade() );
			throw ex;
		}
	}
	
	public List<TotalCompraResponse> filtra( BuscaCompraRequest request ) 
			throws DataIniInvalidaException, 
					DataFimInvalidaException, 
					DataFimAposDataIniException {
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
			throw new DataFimAposDataIniException();
		
		List<Compra> compras = compraRepository.filtra( dataIni, dataFim );
		List<TotalCompraResponse> responses = new ArrayList<>();
		for( Compra c : compras ) {
			TotalCompraResponse resp = totalCompraBuilder.novoTotalCompraResponse();
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
		if ( !compraRepository.existsById( id ) )
			throw new CompraNaoEncontradaException();
		
		compraRepository.deleteById( id );
	}
	
}
