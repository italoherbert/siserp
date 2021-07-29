package italo.siserp.service;

import java.util.ArrayList;
import java.util.Arrays;
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
import italo.siserp.exception.DataCompraException;
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
import italo.siserp.model.request.SaveCategoriaRequest;
import italo.siserp.model.request.SaveCompraParcelaRequest;
import italo.siserp.model.request.SaveCompraRequest;
import italo.siserp.model.request.SaveItemCompraRequest;
import italo.siserp.model.request.SaveItemProdutoRequest;
import italo.siserp.model.request.SaveSubCategoriaRequest;
import italo.siserp.repository.CategoriaMapRepository;
import italo.siserp.repository.CategoriaRepository;
import italo.siserp.repository.CompraRepository;
import italo.siserp.repository.FornecedorRepository;
import italo.siserp.repository.ProdutoRepository;

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
		
		for( SaveItemCompraRequest icreq : request.getItens() ) {
			ItemCompra ic = itemCompraBuilder.novoItemCompra();
			itemCompraBuilder.carregaItemCompra( ic, icreq );
								
			String codigoBarras = ic.getProduto().getCodigoBarras();
			Optional<Produto> pop = produtoRepository.findByCodigoBarras( codigoBarras );
			Produto p;
			if ( pop.isPresent() ) {
				p = pop.get();
			} else {
				p = produtoBuilder.novoProduto();
			}
			
			produtoBuilder.carregaProduto( p, icreq.getProduto() );
			
			List<ItemProduto> produtoItens = new ArrayList<>(); 
			for( SaveItemProdutoRequest ipreq : icreq.getProduto().getItensProdutos() ) {
				ItemProduto ip = this.buscaItemProduto( codigoBarras , ipreq.getCategorias() );
				if ( ip == null ) {		
					ip = itemProdutoBuilder.novoItemProduto();
					
					itemProdutoBuilder.carregaItemProduto( ip, ipreq );
					this.carregaCategorias( ip, ipreq ); 
				} else {
					this.addItemProdutoQuantidade( ip, ipreq ); 
				}
				ip.setProduto( p );
				produtoItens.add( ip );
				
			}			
			p.setItensProdutos( produtoItens );
			
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
		Optional<Fornecedor> fop = fornecedorRepository.findByEmpresa( femp );
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
		List<CategoriaMap> categoriasMap = new ArrayList<>();
		for( SaveCategoriaRequest catreq : request.getCategorias() ) {
			Categoria c;
			Optional<Categoria> opc = categoriaRepository.findByDescricao( catreq.getDescricao() );
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
	
}
