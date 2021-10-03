package italo.siserp.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.siserp.builder.ItemVendaBuilder;
import italo.siserp.builder.LancamentoBuilder;
import italo.siserp.builder.VendaBuilder;
import italo.siserp.builder.VendaParcelaBuilder;
import italo.siserp.dao.CaixaDAO;
import italo.siserp.exception.CaixaNaoAbertoException;
import italo.siserp.exception.ClienteNaoEncontradoException;
import italo.siserp.exception.DataFimInvalidaException;
import italo.siserp.exception.DataIniAposDataFimException;
import italo.siserp.exception.DataIniInvalidaException;
import italo.siserp.exception.DataPagamentoInvalidaException;
import italo.siserp.exception.DataVencimentoInvalidaException;
import italo.siserp.exception.DataVendaInvalidaException;
import italo.siserp.exception.DebitoInvalidoException;
import italo.siserp.exception.DescontoInvalidoException;
import italo.siserp.exception.DoubleInvalidoException;
import italo.siserp.exception.FormaPagInvalidaException;
import italo.siserp.exception.FuncionarioNaoEncontradoException;
import italo.siserp.exception.ParcelaValorInvalidoException;
import italo.siserp.exception.PerfilCaixaRequeridoException;
import italo.siserp.exception.PrecoUnitVendaInvalidoException;
import italo.siserp.exception.ProdutoNaoEncontradoException;
import italo.siserp.exception.QuantidadeInvalidaException;
import italo.siserp.exception.SubtotalInvalidoException;
import italo.siserp.exception.UsuarioLogadoNaoEncontradoException;
import italo.siserp.exception.ValorPagoInvalidoException;
import italo.siserp.exception.VendaNaoEncontradaException;
import italo.siserp.model.Caixa;
import italo.siserp.model.Cliente;
import italo.siserp.model.FormaPag;
import italo.siserp.model.ItemVenda;
import italo.siserp.model.Lancamento;
import italo.siserp.model.LancamentoTipo;
import italo.siserp.model.Produto;
import italo.siserp.model.Venda;
import italo.siserp.model.VendaParcela;
import italo.siserp.model.request.BuscaVendasRequest;
import italo.siserp.model.request.SaveItemVendaRequest;
import italo.siserp.model.request.SaveVendaParcelaRequest;
import italo.siserp.model.request.SaveVendaRequest;
import italo.siserp.model.response.VendaResponse;
import italo.siserp.repository.ClienteRepository;
import italo.siserp.repository.LancamentoRepository;
import italo.siserp.repository.ProdutoRepository;
import italo.siserp.repository.VendaRepository;
import italo.siserp.util.DataUtil;
import italo.siserp.util.FormaPagEnumConversor;
import italo.siserp.util.NumeroUtil;

@Service
public class VendaService {

	@Autowired
	private VendaRepository vendaRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private CaixaDAO caixaDAO;
	
	@Autowired
	private VendaBuilder vendaBuilder;
	
	@Autowired
	private ItemVendaBuilder itemVendaBuilder;
	
	@Autowired
	private LancamentoBuilder lancamentoBuilder;
	
	@Autowired
	private VendaParcelaBuilder vendaParcelaBuilder;
			
	@Autowired
	private DataUtil dataUtil;
	
	@Autowired
	private NumeroUtil numeroUtil;
	
	@Autowired
	private FormaPagEnumConversor enumConversor;
	
	@Transactional
	public void efetuaVenda( Long logadoUID, SaveVendaRequest request ) 
			throws QuantidadeInvalidaException,
				PrecoUnitVendaInvalidoException,
				SubtotalInvalidoException,
				DescontoInvalidoException,
				DebitoInvalidoException,
				DataVendaInvalidaException, 
				ProdutoNaoEncontradoException, 
				CaixaNaoAbertoException, 
				PerfilCaixaRequeridoException,
				ValorPagoInvalidoException,
				ClienteNaoEncontradoException,
				FormaPagInvalidaException, 
				UsuarioLogadoNaoEncontradoException, 
				FuncionarioNaoEncontradoException, 
				ParcelaValorInvalidoException, 
				DataPagamentoInvalidaException, 
				DataVencimentoInvalidaException {				
								
		Caixa caixa = caixaDAO.buscaHojeCaixaBean( logadoUID );
		
		Venda v = vendaBuilder.novoVenda();
		vendaBuilder.carregaVenda( v, request );
		
		List<ItemVenda> itensVenda = new ArrayList<>();
				
		List<SaveItemVendaRequest> itens = request.getItensVenda();
		
		double subtotal = 0;
		for( SaveItemVendaRequest item : itens ) {
			Produto p = produtoRepository.findByCodigoBarras( item.getCodigoBarras() ).orElseThrow( ProdutoNaoEncontradoException::new );
			ItemVenda iv = itemVendaBuilder.novoItemVenda();
			try {
				double quantidade = numeroUtil.stringParaDouble( item.getQuantidade() );
				
				iv.setPrecoUnitario( p.getPrecoUnitarioVenda() );
				iv.setQuantidade( quantidade );
				
				p.setQuantidade( p.getQuantidade() - quantidade );
				
				produtoRepository.save( p );
				
				iv.setVenda( v ); 
				iv.setProduto( p );
				
				itensVenda.add( iv );
				
				subtotal += quantidade * p.getPrecoUnitarioVenda();
			} catch ( DoubleInvalidoException e ) {
				PrecoUnitVendaInvalidoException ex = new PrecoUnitVendaInvalidoException();
				ex.setParams( item.getQuantidade() );
				throw ex;
			}
		}
		
		
		double desconto;		
		try {
			desconto = numeroUtil.stringParaDouble( request.getDesconto() );				
		} catch ( DoubleInvalidoException e ) {
			throw new ValorPagoInvalidoException();
		}						

		double total = subtotal * (1.0d - (desconto/100.0));		
				
		FormaPag formaPag = enumConversor.getFormaPag( request.getFormaPag() );
		if ( formaPag == FormaPag.ESPECIE ) {
			Lancamento lanc = lancamentoBuilder.novoINILancamento( caixa );
			lanc.setTipo( LancamentoTipo.CREDITO );
			lanc.setObs( Lancamento.LANCAMENTO_VENDA_EFETUADA );
			lanc.setValor( total ); 
			lancamentoRepository.save( lanc );			
		}
		
		if ( request.getIncluirCliente().equals( "true" ) ) {
			String clienteNome = request.getClienteNome();
			Optional<Cliente> cop = clienteRepository.buscaPorNome( clienteNome );
			if ( !cop.isPresent() )
				throw new ClienteNaoEncontradoException();
		
			v.setCliente( cop.get() );			
		}
		
		List<VendaParcela> parcelas = new ArrayList<>();
		for( SaveVendaParcelaRequest pReq : request.getParcelas() ) {
			VendaParcela parcela = new VendaParcela();
			vendaParcelaBuilder.carregaVendaParcela( parcela, pReq );
			
			parcela.setVenda( v );			
			parcelas.add( parcela );
		}
		v.setParcelas( parcelas );
		
		v.setItensVenda( itensVenda );
		v.setCaixa( caixa );
		
		vendaRepository.save( v );				
	}
		
	public void atualizaVenda( Long id, SaveVendaRequest req )
			throws VendaNaoEncontradaException,
					DataVendaInvalidaException,
					SubtotalInvalidoException,
					DescontoInvalidoException,
					DebitoInvalidoException,
					PrecoUnitVendaInvalidoException,
					QuantidadeInvalidaException,
					FormaPagInvalidaException {
		
		Venda v = vendaRepository.findById( id ).orElseThrow( VendaNaoEncontradaException::new );
						
		vendaBuilder.carregaVenda( v, req );
		
		vendaRepository.save( v );
	}
	
	public List<VendaResponse> filtra( BuscaVendasRequest request ) 
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
		
		
		List<Venda> vendas = vendaRepository.filtra( dataIni, dataFim );
				
		List<VendaResponse> responses = new ArrayList<>();
		
		for( Venda v : vendas ) {			
			VendaResponse resp = vendaBuilder.novoVendaResponse();
			vendaBuilder.carregaVendaResponse( resp, v );
			
			responses.add( resp );
		}
		
		return responses;
	}
	
	public List<VendaResponse> buscaVendasPorClienteId( Long clienteId ) {
		List<Venda> vendas = vendaRepository.buscaVendasPorClienteId( clienteId );
		
		List<VendaResponse> lista = new ArrayList<>();
		for( Venda v : vendas ) {
			VendaResponse resp = vendaBuilder.novoVendaResponse();
			vendaBuilder.carregaVendaResponse( resp, v );
			
			lista.add( resp );
		}
		return lista;
	}
	
	public List<VendaResponse> buscaVendasEmDebitoPorClienteId( Long clienteId ) {
		List<Venda> vendas = vendaRepository.buscaVendasEmDebitoPorClienteId( clienteId );
		
		List<VendaResponse> lista = new ArrayList<>();
		for( Venda v : vendas ) {
			VendaResponse resp = vendaBuilder.novoVendaResponse();
			vendaBuilder.carregaVendaResponse( resp, v );
			
			lista.add( resp );
		}
		return lista;
	}
	
	public VendaResponse buscaVendaPorId( Long id ) throws VendaNaoEncontradaException {
		Venda v = vendaRepository.findById( id ).orElseThrow( VendaNaoEncontradaException::new );
				
		VendaResponse resp = vendaBuilder.novoVendaResponse();
		vendaBuilder.carregaVendaResponse( resp, v ); 
		
		return resp;
	}
			
	@Transactional
	public void deleta( Long id ) throws VendaNaoEncontradaException {
		Venda v = vendaRepository.findById( id ).orElseThrow( VendaNaoEncontradaException::new );
		
		List<ItemVenda> itens = v.getItensVenda();
		
		if ( v.getFormaPag() == FormaPag.APRAZO ) {
			double subtotal = 0;
			for( ItemVenda item : itens )												
				subtotal += item.getQuantidade() * item.getPrecoUnitario();											
					
			double total = subtotal * ( 1.0d - ( v.getDesconto() / 100.0 ) );
									
			Caixa caixa = v.getCaixa();			
			
			Lancamento lanc = lancamentoBuilder.novoINILancamento( caixa );
			lanc.setTipo( LancamentoTipo.DEBITO );
			lanc.setObs( Lancamento.LANCAMENTO_VENDA_CANCELADA ); 
			lanc.setValor( total ); 
			lancamentoRepository.save( lanc );			
		}
		
		for( ItemVenda item : itens ) {
			Produto p = item.getProduto();			
			p.setQuantidade( p.getQuantidade() + item.getQuantidade() );
			
			produtoRepository.save( p );
		}
						
		vendaRepository.delete( v ); 
	}		
			
}
