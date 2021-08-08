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
import italo.siserp.exception.LongInvalidoException;
import italo.siserp.exception.PerfilCaixaRequeridoException;
import italo.siserp.exception.PrecoUnitVendaInvalidoException;
import italo.siserp.exception.ProdutoNaoEncontradoException;
import italo.siserp.exception.QuantidadeInvalidaException;
import italo.siserp.exception.SubtotalInvalidoException;
import italo.siserp.exception.ValorPagoInvalidoException;
import italo.siserp.exception.VendaNaoEncontradaException;
import italo.siserp.model.Caixa;
import italo.siserp.model.Cliente;
import italo.siserp.model.FormaPag;
import italo.siserp.model.ItemVenda;
import italo.siserp.model.Produto;
import italo.siserp.model.Venda;
import italo.siserp.model.request.BuscaVendasRequest;
import italo.siserp.model.request.EfetuarPagamentoRequest;
import italo.siserp.model.request.SaveItemVendaRequest;
import italo.siserp.model.request.SaveVendaRequest;
import italo.siserp.model.response.EfetuarVendaPagamentoResponse;
import italo.siserp.model.response.QuitarDebitoResponse;
import italo.siserp.model.response.VendaResponse;
import italo.siserp.repository.CaixaRepository;
import italo.siserp.repository.ClienteRepository;
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
	private CaixaRepository caixaRepository;
		
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
	public EfetuarVendaPagamentoResponse efetuaVenda( Caixa caixa, SaveVendaRequest request ) 
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
				FormaPagInvalidaException {				
								
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
		double troco = 0;
		
		FormaPag formaPag = enumConversor.getFormaPag( request.getFormaPag() );
		if ( formaPag == FormaPag.ESPECIE ) {			
			double valorPag;
			try {
				valorPag = numeroUtil.stringParaDouble( request.getValorPago() );				
			} catch ( DoubleInvalidoException e ) {
				throw new ValorPagoInvalidoException();
			}
					
			caixa.setValor( caixa.getValor() + total );
			caixaRepository.save( caixa );
			
			troco = valorPag - total;
		} else if ( formaPag == FormaPag.DEBITO ) {
			v.setDebito( subtotal * desconto ); 
		}
		
		if ( request.getIncluirCliente().equals( "true" ) ) {
			try {
				Long clienteId = numeroUtil.stringParaLong( request.getClienteId() );
			
				Optional<Cliente> cop = clienteRepository.findById( clienteId );
				if ( !cop.isPresent() )
					throw new ClienteNaoEncontradoException();
			
				v.setCliente( cop.get() );
			} catch ( LongInvalidoException e ) {
				throw new ClienteNaoEncontradoException();
			}
		}
		
		v.setItensVenda( itensVenda );
		v.setCaixa( caixa );
		
		vendaRepository.save( v );
				
		EfetuarVendaPagamentoResponse resp = new EfetuarVendaPagamentoResponse();
		resp.setTroco( troco );
		return resp;
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
			
	public void deleta( Long id ) throws VendaNaoEncontradaException {
		if ( !vendaRepository.existsById( id ) )
			throw new VendaNaoEncontradaException();
		
		vendaRepository.deleteById( id ); 
	}
	
	@Transactional
	public QuitarDebitoResponse efetuarPagamento( Caixa caixa, EfetuarPagamentoRequest request ) 
			throws ClienteNaoEncontradoException, ValorPagoInvalidoException {
		
		Long clienteId = request.getClienteId();
		Cliente c = clienteRepository.findById( clienteId ).orElseThrow( ClienteNaoEncontradoException::new );
		
		List<Venda> vendas = c.getVendas();
		double valor = 0;
		try {
			valor = numeroUtil.stringParaDouble( request.getValorPago() );			
		} catch (DoubleInvalidoException e) {
			throw new ValorPagoInvalidoException();
		}
		
		caixa.setValor( caixa.getValor() + valor );
		caixaRepository.save( caixa );
		
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
