package italo.siserp.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.siserp.builder.CaixaBuilder;
import italo.siserp.builder.LancamentoBuilder;
import italo.siserp.dao.CaixaDAO;
import italo.siserp.exception.CaixaJaAbertoException;
import italo.siserp.exception.CaixaNaoAbertoException;
import italo.siserp.exception.CaixaNaoEncontradoException;
import italo.siserp.exception.CaixaValorInicialInvalidoException;
import italo.siserp.exception.DataDiaInvalidaException;
import italo.siserp.exception.DataFimInvalidaException;
import italo.siserp.exception.DataIniAposDataFimException;
import italo.siserp.exception.DataIniInvalidaException;
import italo.siserp.exception.FuncionarioNaoEncontradoException;
import italo.siserp.exception.LancamentoTipoInvalidoException;
import italo.siserp.exception.LancamentoValorInvalidoException;
import italo.siserp.exception.PerfilCaixaRequeridoException;
import italo.siserp.exception.UsuarioNaoEncontradoException;
import italo.siserp.model.Caixa;
import italo.siserp.model.FormaPag;
import italo.siserp.model.Funcionario;
import italo.siserp.model.Lancamento;
import italo.siserp.model.LancamentoTipo;
import italo.siserp.model.Venda;
import italo.siserp.model.request.AbreCaixaRequest;
import italo.siserp.model.request.BuscaBalancosDiarios;
import italo.siserp.model.request.BuscaCaixasPorDataDiaRequest;
import italo.siserp.model.request.BuscaCaixasRequest;
import italo.siserp.model.request.FechaCaixaRequest;
import italo.siserp.model.response.BalancoDiarioResponse;
import italo.siserp.model.response.CaixaBalancoResponse;
import italo.siserp.model.response.CaixaResponse;
import italo.siserp.repository.CaixaRepository;
import italo.siserp.repository.LancamentoRepository;
import italo.siserp.util.DataUtil;
import italo.siserp.util.NumeroUtil;

@Service
public class CaixaService {
	
	@Autowired
	private CaixaRepository caixaRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
					
	@Autowired
	private CaixaBuilder caixaBuilder;
	
	@Autowired
	private LancamentoBuilder lancamentoBuilder;
		
	@Autowired
	private CaixaDAO caixaDAO;
	
	@Autowired
	private DataUtil dataUtil;
					
	@Autowired
	private NumeroUtil numeroUtil;
	
	public void abreCaixa( Long usuarioId, AbreCaixaRequest req ) 
			throws PerfilCaixaRequeridoException,
				UsuarioNaoEncontradoException, 
				FuncionarioNaoEncontradoException,
				CaixaValorInicialInvalidoException,
				CaixaJaAbertoException, 
				LancamentoTipoInvalidoException,
				LancamentoValorInvalidoException {
		
		Funcionario f = caixaDAO.buscaFuncionarioPorUID( usuarioId );		
		
		Date hoje = dataUtil.apenasData( new Date() );
		
		Optional<Caixa> cop = caixaRepository.buscaCaixa( f.getId(), hoje );
		if ( cop.isPresent() )
			throw new CaixaJaAbertoException();
		
		Caixa caixa = caixaBuilder.novoCaixa();
		caixaBuilder.carregaCaixa( caixa, req ); 
		
		Lancamento lanc = lancamentoBuilder.novoLancamento();
		lancamentoBuilder.carregaLancamento( lanc, req.getLancamento() );

		lanc.setObs( Lancamento.LANCAMENTO_ABRE_CAIXA ); 	
		lanc.setTipo( LancamentoTipo.CREDITO );
		
		caixa.setLancamentos( Arrays.asList( lanc ) );					
		lanc.setCaixa( caixa ); 

		caixa.setFuncionario( f );
		f.setCaixas( Arrays.asList( caixa ) );
				
		caixaRepository.save( caixa );		
	}
	
	public void fechaCaixa( Long usuarioId, FechaCaixaRequest request ) 
			throws PerfilCaixaRequeridoException, 
				CaixaNaoAbertoException, 
				UsuarioNaoEncontradoException, 
				FuncionarioNaoEncontradoException, 
				LancamentoTipoInvalidoException, 
				LancamentoValorInvalidoException {
		
		Caixa caixa = caixaDAO.buscaHojeCaixaBean( usuarioId );
		
		Lancamento lanc = lancamentoBuilder.novoLancamento();
		lancamentoBuilder.carregaLancamento( lanc, request.getLancamento() );
		
		lanc.setObs( Lancamento.LANCAMENTO_FECHA_CAIXA ); 
		lanc.setTipo( LancamentoTipo.DEBITO );
		
		caixa.setLancamentos( Arrays.asList( lanc ) );					
		lanc.setCaixa( caixa );
		
		lancamentoRepository.save( lanc );
	}
	
	public List<BalancoDiarioResponse> geraBalancosDiarios( BuscaBalancosDiarios request ) 
			throws DataIniInvalidaException, 
				DataFimInvalidaException, 
				DataIniAposDataFimException {
		Date dataIni, dataFim;
		try {
			dataIni = dataUtil.apenasData( dataUtil.stringParaData( request.getDataIni() ) );			
		} catch ( ParseException e ) {
			throw new DataIniInvalidaException();
		}
		try {
			dataFim = dataUtil.apenasData( dataUtil.stringParaData( request.getDataFim() ) );			
		} catch ( ParseException e ) {
			throw new DataFimInvalidaException();
		}
		
		if ( dataIni.after( dataFim ) )
			throw new DataIniAposDataFimException();
		
		Date d = dataIni;
		
		List<BalancoDiarioResponse> balancos = new ArrayList<>();
		while( d.compareTo( dataFim ) <= 0 ) {
			List<Caixa> caixas = caixaRepository.listaPorDataDia( d );
			
			double debito = 0;
			double credito = 0;
			double saldo = 0;
			
			double totalVendasAPrazo = 0;
			double cartaoValorRecebido = 0;
			
			for( Caixa c : caixas ) {
				CaixaBalancoResponse resp = this.geraCaixaBalanco( c );
				debito += Double.parseDouble( resp.getDebito() );
				credito += Double.parseDouble( resp.getCredito() );
				saldo += Double.parseDouble( resp.getSaldo() );
				totalVendasAPrazo += Double.parseDouble( resp.getTotalVendasAPrazo() );
				cartaoValorRecebido += Double.parseDouble( resp.getCartaoValorRecebido() );
			}
			
			BalancoDiarioResponse balanco = new BalancoDiarioResponse();
			balanco.setDataAbertura( dataUtil.dataParaString( d ) ); 
			balanco.setDebito( numeroUtil.doubleParaString( debito ) );
			balanco.setCredito( numeroUtil.doubleParaString( credito ) );
			balanco.setSaldo( numeroUtil.doubleParaString( saldo ) );
			balanco.setTotalVendasAPrazo( numeroUtil.doubleParaString( totalVendasAPrazo ) );
			balanco.setCartaoValorRecebido( numeroUtil.doubleParaString( cartaoValorRecebido ) ); 
			
			balancos.add( balanco );
			
			d = dataUtil.addUmDia( d );
		}
		
		return balancos;
	}
	
	public List<CaixaResponse> listaCaixasPorDataDia( BuscaCaixasPorDataDiaRequest request ) throws DataDiaInvalidaException {		
		try {
			Date d = dataUtil.stringParaData( request.getDataDia() );
			
			List<Caixa> caixas = caixaRepository.listaPorDataDia( d );
			
			List<CaixaResponse> lista = new ArrayList<>();
			for( Caixa c : caixas ) {
				CaixaResponse resp = caixaBuilder.novoCaixaResponse();
				caixaBuilder.carregaCaixaResponse( resp, c );
				
				lista.add( resp );
			}
			return lista;
		} catch ( ParseException e ) {
			throw new DataDiaInvalidaException();
		}
	}	

	public CaixaBalancoResponse geraCaixaBalancoHoje( Long usuarioId ) 
			throws PerfilCaixaRequeridoException, 
				CaixaNaoAbertoException, 
				UsuarioNaoEncontradoException, 
				FuncionarioNaoEncontradoException {
		
		Caixa c = caixaDAO.buscaHojeCaixaBean( usuarioId );						
		return this.geraCaixaBalanco( c );
	}
	
	public CaixaBalancoResponse geraCaixaBalanco( Caixa c ) {
		
		List<Lancamento> lancamentos = c.getLancamentos();
		
		Date dataAbertura = c.getDataAbertura();
		
		double debito = 0;
		double credito = 0;
		for( Lancamento l : lancamentos ) {
			if ( l.getTipo() == LancamentoTipo.DEBITO ) {
				debito += l.getValor();
			} else if ( l.getTipo() == LancamentoTipo.CREDITO ) {
				credito += l.getValor();
			}
		}		
		
		double saldo = credito - debito;
		
		double cartaoValorRecebido = 0;
		double totalVendasAPrazo = 0;
		List<Venda> vendas = c.getVendas();
		for( Venda v : vendas ) {
			double total = v.getSubtotal() * (1.0d - v.getDesconto() );
			if ( v.getFormaPag() == FormaPag.CARTAO )
				cartaoValorRecebido += total;			
			if ( v.getFormaPag() == FormaPag.DEBITO ) 
				totalVendasAPrazo += total;
		}
		
		CaixaBalancoResponse resp = new CaixaBalancoResponse();
		resp.setFuncionarioNome( c.getFuncionario().getPessoa().getNome() ); 
		resp.setDataAbertura( dataUtil.dataParaString( dataAbertura ) );
		resp.setDebito( numeroUtil.doubleParaString( debito ) );
		resp.setCredito( numeroUtil.doubleParaString( credito ) );
		resp.setSaldo( numeroUtil.doubleParaString( saldo ) ); 
		resp.setCartaoValorRecebido( numeroUtil.doubleParaString( cartaoValorRecebido ) );
		resp.setTotalVendasAPrazo( numeroUtil.doubleParaString( totalVendasAPrazo ) );
		return resp;
	}
	
	public List<CaixaResponse> filtra( BuscaCaixasRequest request ) 
			throws DataIniInvalidaException, 
				DataFimInvalidaException, 
				DataIniAposDataFimException {
		Date dataIni, dataFim;
		
		try {
			dataIni = dataUtil.apenasData( dataUtil.stringParaData( request.getDataIni() ) );
		} catch (ParseException e) {
			throw new DataIniInvalidaException();
		}
		try {
			dataFim = dataUtil.apenasData( dataUtil.stringParaData( request.getDataFim() ) );
		} catch (ParseException e) {
			throw new DataIniInvalidaException();
		}
		
		if ( dataIni.after( dataFim ) )
			throw new DataIniAposDataFimException();
		
		List<Caixa> caixas;
		if ( request.getIncluirFuncionario().equals( "true" ) ) {
			String funcNomeIni = request.getFuncionarioNomeIni()+"%";
			caixas = caixaRepository.filtra( dataIni, dataFim, funcNomeIni );
		} else {
			caixas = caixaRepository.filtraSemFuncionario( dataIni, dataFim );
		}
		
		List<CaixaResponse> responses = new ArrayList<>();
		for( Caixa c : caixas ) {
			CaixaResponse resp = caixaBuilder.novoCaixaResponse();
			caixaBuilder.carregaCaixaResponse( resp, c );
			
			responses.add( resp );
		}
		
		return responses;
	}
	
	public CaixaResponse buscaCaixa( Long caixaId ) throws CaixaNaoEncontradoException {
		Caixa c = caixaRepository.findById( caixaId ).orElseThrow( CaixaNaoEncontradoException::new );
		
		CaixaResponse resp = caixaBuilder.novoCaixaResponse();
		caixaBuilder.carregaCaixaResponse( resp, c );
		
		return resp;
	}
	
	public CaixaResponse buscaCaixaHoje( Long usuarioId ) 
			throws PerfilCaixaRequeridoException, 
				CaixaNaoAbertoException, 
				UsuarioNaoEncontradoException, 
				FuncionarioNaoEncontradoException {
				
		Caixa c = caixaDAO.buscaHojeCaixaBean( usuarioId );
		
		CaixaResponse resp = caixaBuilder.novoCaixaResponse();
		caixaBuilder.carregaCaixaResponse( resp, c );
		
		return resp;					
	}
				
	public void deleteCaixa( Long caixaId ) throws CaixaNaoEncontradoException {
		if ( !caixaRepository.findById( caixaId ).isPresent() )
			throw new CaixaNaoEncontradoException();
		
		caixaRepository.deleteById( caixaId ); 
	}	
	
}
