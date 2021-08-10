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
import italo.siserp.builder.VendaBuilder;
import italo.siserp.dao.CaixaDAO;
import italo.siserp.exception.CaixaNaoAbertoException;
import italo.siserp.exception.ClienteNaoEncontradoException;
import italo.siserp.exception.DataFimAposDataIniException;
import italo.siserp.exception.DataFimInvalidaException;
import italo.siserp.exception.DataIniInvalidaException;
import italo.siserp.exception.DataVendaInvalidaException;
import italo.siserp.exception.DebitoInvalidoException;
import italo.siserp.exception.DescontoInvalidoException;
import italo.siserp.exception.DoubleInvalidoException;
import italo.siserp.exception.FormaPagInvalidaException;
import italo.siserp.exception.FuncionarioNaoEncontradoException;
import italo.siserp.exception.PerfilCaixaRequeridoException;
import italo.siserp.exception.PrecoUnitVendaInvalidoException;
import italo.siserp.exception.ProdutoNaoEncontradoException;
import italo.siserp.exception.QuantidadeInvalidaException;
import italo.siserp.exception.SubtotalInvalidoException;
import italo.siserp.exception.UsuarioNaoEncontradoException;
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
import italo.siserp.model.request.BuscaVendasRequest;
import italo.siserp.model.request.EfetuarPagamentoRequest;
import italo.siserp.model.request.SaveItemVendaRequest;
import italo.siserp.model.request.SaveVendaRequest;
import italo.siserp.model.response.QuitarDebitoResponse;
import italo.siserp.model.response.VendaResponse;
import italo.siserp.repository.ClienteRepository;
import italo.siserp.repository.LancamentoRepository;
import italo.siserp.repository.ProdutoRepository;
import italo.siserp.repository.VendaRepository;
import italo.siserp.util.DataUtil;
import italo.siserp.util.NumeroUtil;
import italo.siserp.util.enums_tipo.FormaPagTipoEnumConversor;

@Service
public class VendaService {

	@Autowired
	private VendaRepository vendaRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private LancamentoRepository lancamentoRepository;
			
	@Autowired
	private CaixaDAO caixaDAO;
	
	@Autowired
	private VendaBuilder vendaBuilder;
	
	@Autowired
	private ItemVendaBuilder itemVendaBuilder;
	
	@Autowired
	private ClienteRepository clienteRepository;
		
	@Autowired
	private DataUtil dataUtil;
	
	@Autowired
	private NumeroUtil numeroUtil;
	
	@Autowired
	private FormaPagTipoEnumConversor enumConversor;
	
	@Transactional
	public void efetuaVenda( Long usuarioId, SaveVendaRequest request ) 
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
				UsuarioNaoEncontradoException, 
				FuncionarioNaoEncontradoException {				
								
		Caixa caixa = caixaDAO.buscaHojeCaixaBean( usuarioId );
		
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

		double total = subtotal * (1.0d - desconto);				
		
		FormaPag formaPag = enumConversor.getFormaPag( request.getFormaPag() );
		if ( formaPag == FormaPag.ESPECIE ) {
			Lancamento lanc = new Lancamento();
			lanc.setCaixa( caixa );
			lanc.setDataOperacao( new Date() );
			lanc.setTipo( LancamentoTipo.CREDITO );
			lanc.setObs( Lancamento.LANCAMENTO_VENDA_EFETUADA );
			lanc.setValor( total ); 
			lancamentoRepository.save( lanc );			
		} else if ( formaPag == FormaPag.DEBITO ) {
			v.setDebito( total ); 
		}
		
		if ( request.getIncluirCliente().equals( "true" ) ) {
			String clienteNome = request.getClienteNome();
			Optional<Cliente> cop = clienteRepository.buscaPorNome( clienteNome );
			if ( !cop.isPresent() )
				throw new ClienteNaoEncontradoException();
		
			v.setCliente( cop.get() );			
		}
		
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
					DataFimAposDataIniException {
		boolean incluirCliente = request.getIncluirCliente().equals( "true" );				
		
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
		
		List<Venda> vendas;
		if ( incluirCliente ) {
			String nomeIni = (request.getClienteNomeIni().equals( "*" ) ? "" : request.getClienteNomeIni() );
			vendas = vendaRepository.filtra( dataIni, dataFim, nomeIni+"%" );
		} else {
			vendas = vendaRepository.filtraSemCliente( dataIni, dataFim );
		}
		
		List<VendaResponse> responses = new ArrayList<>();
		
		for( Venda v : vendas ) {			
			VendaResponse resp = vendaBuilder.novoVendaResponse();
			vendaBuilder.carregaVendaResponse( resp, v );
			
			responses.add( resp );
		}
		
		return responses;
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
		
		if ( v.getFormaPag() == FormaPag.ESPECIE ) {
			double subtotal = 0;
			for( ItemVenda item : itens )												
				subtotal += item.getQuantidade() * item.getPrecoUnitario();											
					
			double total = subtotal * ( 1.0d - v.getDesconto() );
									
			System.out.println( "TOTAL= "+total+"  "+subtotal+"  "+v.getDesconto() );
			Caixa caixa = v.getCaixa();			
			
			Lancamento lanc = new Lancamento();
			lanc.setCaixa( caixa );
			lanc.setDataOperacao( new Date() );
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
	
	@Transactional
	public QuitarDebitoResponse efetuarPagamento( Long usuarioId, EfetuarPagamentoRequest request ) 
			throws ClienteNaoEncontradoException, 
			ValorPagoInvalidoException, 
			PerfilCaixaRequeridoException, 
			CaixaNaoAbertoException,
			UsuarioNaoEncontradoException, 
			FuncionarioNaoEncontradoException {
		
		Caixa caixa = caixaDAO.buscaHojeCaixaBean( usuarioId );
		
		Long clienteId = request.getClienteId();
		Cliente c = clienteRepository.findById( clienteId ).orElseThrow( ClienteNaoEncontradoException::new );
		
		List<Venda> vendas = c.getVendas();
		double valor = 0;
		try {
			valor = numeroUtil.stringParaDouble( request.getValorPago() );			
		} catch (DoubleInvalidoException e) {
			throw new ValorPagoInvalidoException();
		}
		
		Lancamento lanc = new Lancamento();
		lanc.setCaixa( caixa );
		lanc.setDataOperacao( new Date() );
		lanc.setTipo( LancamentoTipo.CREDITO );
		lanc.setValor( valor ); 
		lancamentoRepository.save( lanc );
				
		int size = vendas.size();
		double debitoRestante = 0;
		for( int i = 0; i < size; i++ ) {
			Venda v = vendas.get( i );
			if ( valor > 0 ) {
				if ( valor >= v.getDebito() ) {
					v.setDebito( 0 );
					valor -= v.getDebito();
				} else {
					v.setDebito( v.getDebito() - valor );
					valor = 0;
				}
			}
			debitoRestante += v.getDebito();
			
			vendaRepository.save( v );
		}
								
		QuitarDebitoResponse resp = new QuitarDebitoResponse();
		resp.setTroco( valor );
		resp.setDebitoRestante( debitoRestante ); 
		return resp;
	}
			
}
