package italo.siserp.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.siserp.builder.ContasReceberBuilder;
import italo.siserp.builder.LancamentoBuilder;
import italo.siserp.dao.CaixaDAO;
import italo.siserp.exception.CaixaNaoAbertoException;
import italo.siserp.exception.ClienteNaoEncontradoException;
import italo.siserp.exception.DataFimInvalidaException;
import italo.siserp.exception.DataIniAposDataFimException;
import italo.siserp.exception.DataIniInvalidaException;
import italo.siserp.exception.DoubleInvalidoException;
import italo.siserp.exception.FuncionarioNaoEncontradoException;
import italo.siserp.exception.LongInvalidoException;
import italo.siserp.exception.PerfilCaixaRequeridoException;
import italo.siserp.exception.UsuarioNaoEncontradoException;
import italo.siserp.exception.ValorRecebidoInvalidoException;
import italo.siserp.model.Caixa;
import italo.siserp.model.Cliente;
import italo.siserp.model.Lancamento;
import italo.siserp.model.LancamentoTipo;
import italo.siserp.model.Venda;
import italo.siserp.model.VendaParcela;
import italo.siserp.repository.ClienteRepository;
import italo.siserp.repository.LancamentoRepository;
import italo.siserp.repository.VendaParcelaRepository;
import italo.siserp.service.request.BuscaContasReceberRequest;
import italo.siserp.service.request.EfetuarRecebimentoRequest;
import italo.siserp.service.response.ContasReceberResponse;
import italo.siserp.util.DataUtil;
import italo.siserp.util.NumeroUtil;

@Service
public class ContasReceberService  {
			
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private VendaParcelaRepository vendaParcelaRepository;
	
	@Autowired
	private CaixaDAO caixaDAO;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private ContasReceberBuilder contasReceberBuilder;
	
	@Autowired
	private LancamentoBuilder lancamentoBuilder;
		
	@Autowired
	private DataUtil dataUtil;
	
	@Autowired
	private NumeroUtil numeroUtil;
				
	@Transactional
	public void efetuaRecebimento( Long usuarioId, EfetuarRecebimentoRequest request ) 
			throws ClienteNaoEncontradoException, 
				ValorRecebidoInvalidoException, 
				PerfilCaixaRequeridoException, 
				CaixaNaoAbertoException, 
				UsuarioNaoEncontradoException,
				FuncionarioNaoEncontradoException, 
				LongInvalidoException {
		
		Long clienteId = numeroUtil.stringParaLong( request.getClienteId() );
		
		Caixa caixa = caixaDAO.buscaHojeCaixaBean( usuarioId );
		Cliente cliente = clienteRepository.findById( clienteId ).orElseThrow( ClienteNaoEncontradoException::new );		
		
		double valor;
		try {
			valor = numeroUtil.stringParaDouble( request.getValorPago() );
		} catch (DoubleInvalidoException e) {
			throw new ValorRecebidoInvalidoException();
		} 
	
		double debito = valor;
		
		List<Venda> vendas = cliente.getVendas();
		int size = vendas.size();
		for( int i = 0; debito > 0 && i < size; i++ ) {
			Venda v = vendas.get( i );

			List<VendaParcela> parcelas = v.getParcelas();
			int size2 = parcelas.size();
			for( int j = 0; debito > 0 && j < size2; j++ ) {
				VendaParcela p = parcelas.get( j );
				if ( valor > p.getDebito() ) {
					debito -= p.getDebito();
					p.setDebito( 0 ); 
				} else {
					p.setDebito( p.getDebito() - debito );
					debito = 0;
				}
			
				vendaParcelaRepository.save( p );												
			}
		}		
		
		Lancamento l = lancamentoBuilder.novoINILancamento( caixa );
		l.setTipo( LancamentoTipo.CREDITO );
		l.setObs( Lancamento.CLIENTE_PAGOU ); 
		l.setValor( valor - debito );
		
		lancamentoRepository.save( l );
	}
	
	public ContasReceberResponse filtra( BuscaContasReceberRequest request ) 
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
		
		boolean incluirPagas = request.getIncluirPagas().equals( "true" );
		boolean incluirCliente = request.getIncluirCliente().equals( "true" );
				
		List<VendaParcela> vendas;
		if ( incluirCliente ) {
			String nomeIni = (request.getClienteNomeIni().equals( "*" ) ? "" : request.getClienteNomeIni() );
			vendas = vendaParcelaRepository.filtra( dataIni, dataFim, nomeIni+"%", incluirPagas );
		} else {
			vendas = vendaParcelaRepository.filtraSemCliente( dataIni, dataFim, incluirPagas );
		}
		
		Double totalCompleto = vendaParcelaRepository.calculaDebitoTotalCompleto();
		if ( totalCompleto == null )
			totalCompleto = 0d;
		
		ContasReceberResponse resp = contasReceberBuilder.novoContasReceberResponse();
		contasReceberBuilder.carregaContasReceberResponse( resp, vendas, totalCompleto, incluirPagas );
		
		return resp;
	}
	
}
