package italo.siserp.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.siserp.builder.CaixaBuilder;
import italo.siserp.builder.LancamentoBuilder;
import italo.siserp.exception.CaixaJaAbertoException;
import italo.siserp.exception.CaixaNaoAbertoException;
import italo.siserp.exception.CaixaNaoEncontradoException;
import italo.siserp.exception.CaixaValorInicialInvalidoException;
import italo.siserp.exception.DataFimAposDataIniException;
import italo.siserp.exception.DataFimInvalidaException;
import italo.siserp.exception.DataIniInvalidaException;
import italo.siserp.exception.FuncionarioNaoEncontradoException;
import italo.siserp.exception.LancamentoTipoInvalidoException;
import italo.siserp.exception.LancamentoValorInvalidoException;
import italo.siserp.exception.PerfilCaixaRequeridoException;
import italo.siserp.exception.UsuarioNaoEncontradoException;
import italo.siserp.model.Caixa;
import italo.siserp.model.Funcionario;
import italo.siserp.model.Lancamento;
import italo.siserp.model.LancamentoTipo;
import italo.siserp.model.Usuario;
import italo.siserp.model.UsuarioTipo;
import italo.siserp.model.request.AbreCaixaRequest;
import italo.siserp.model.request.BuscaCaixasRequest;
import italo.siserp.model.request.SaveLancamentoRequest;
import italo.siserp.model.response.CaixaBalancoResponse;
import italo.siserp.model.response.CaixaResponse;
import italo.siserp.repository.CaixaRepository;
import italo.siserp.repository.LancamentoRepository;
import italo.siserp.repository.UsuarioRepository;
import italo.siserp.util.DataUtil;
import italo.siserp.util.NumeroUtil;

@Service
public class CaixaService {
	
	@Autowired
	private CaixaRepository caixaRepository;
		
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private CaixaBuilder caixaBuilder;
	
	@Autowired
	private LancamentoBuilder lancamentoBuilder;
	
	@Autowired
	private DataUtil dataUtil;
	
	@Autowired
	private NumeroUtil numeroUtil;
		
	public void abreGetCaixaSeNaoAberto( Long usuarioId, AbreCaixaRequest req ) 
			throws PerfilCaixaRequeridoException,
				UsuarioNaoEncontradoException, 
				FuncionarioNaoEncontradoException,
				CaixaValorInicialInvalidoException,
				CaixaJaAbertoException, 
				LancamentoTipoInvalidoException,
				LancamentoValorInvalidoException {
		
		Funcionario f = this.buscaFuncionarioPorUID( usuarioId );		
		
		Date hoje = dataUtil.apenasData( new Date() );
		
		Optional<Caixa> cop = caixaRepository.buscaCaixa( f.getId(), hoje );
		if ( cop.isPresent() )
			throw new CaixaJaAbertoException();
		
		Caixa caixa = caixaBuilder.novoCaixa();
		caixaBuilder.carregaAbreCaixa( caixa, req ); 
		
		Lancamento lanc = lancamentoBuilder.novoLancamento();
		lancamentoBuilder.carregaLancamento( lanc, req.getLancamento() );
		
		lanc.setTipo( LancamentoTipo.CREDITO );
		
		caixa.setLancamentos( Arrays.asList( lanc ) );					
		lanc.setCaixa( caixa ); 

		caixa.setFuncionario( f );
		f.setCaixas( Arrays.asList( caixa ) );
				
		caixaRepository.save( caixa );		
	}

	@Transactional
	public void deletaLancamentos( Long usuarioId ) 
			throws PerfilCaixaRequeridoException, 
				CaixaNaoAbertoException, 
				UsuarioNaoEncontradoException,
				FuncionarioNaoEncontradoException {
		
		Caixa c = this.buscaHojeCaixaBean( usuarioId );
		
		List<Lancamento> lancamentos = new ArrayList<>( c.getLancamentos() );
		for( Lancamento l : lancamentos ) {
			c.getLancamentos().remove( l );
			lancamentoRepository.delete( l ); 
		}
	}
				
	public void efetuaLancamento( Long usuarioId, SaveLancamentoRequest request ) 
			throws PerfilCaixaRequeridoException, 
				CaixaNaoAbertoException, 
				LancamentoTipoInvalidoException, 
				LancamentoValorInvalidoException, 
				UsuarioNaoEncontradoException, 
				FuncionarioNaoEncontradoException {
		
		Caixa c = this.buscaHojeCaixaBean( usuarioId );

		Lancamento lanc = lancamentoBuilder.novoLancamento();
		lancamentoBuilder.carregaLancamento( lanc, request );
	
		lanc.setCaixa( c );
		
		lancamentoRepository.save( lanc );
	}
	
	public CaixaBalancoResponse geraBalanco( Long usuarioId ) 
			throws PerfilCaixaRequeridoException, 
				CaixaNaoAbertoException, 
				UsuarioNaoEncontradoException, 
				FuncionarioNaoEncontradoException {
		
		Caixa c = this.buscaHojeCaixaBean( usuarioId );
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
		
		CaixaBalancoResponse resp = new CaixaBalancoResponse();
		resp.setFuncionarioNome( c.getFuncionario().getPessoa().getNome() ); 
		resp.setDataAbertura( dataUtil.dataParaString( dataAbertura ) );
		resp.setDebito( numeroUtil.doubleParaString( debito ) );
		resp.setCredito( numeroUtil.doubleParaString( credito ) );
		resp.setSaldo( numeroUtil.doubleParaString( saldo ) ); 
		return resp;
	}
	
	public List<CaixaResponse> filtra( BuscaCaixasRequest request ) 
			throws DataIniInvalidaException, 
				DataFimInvalidaException, 
				DataFimAposDataIniException {
		Date dataIni, dataFim;
		
		try {
			dataIni = dataUtil.apenasData( dataUtil.stringParaData( request.getDataIni() ) );
			System.out.println( "Dataini= "+dataIni );
		} catch (ParseException e) {
			throw new DataIniInvalidaException();
		}
		try {
			dataFim = dataUtil.apenasData( dataUtil.stringParaData( request.getDataFim() ) );
			System.out.println( "Datafim= "+dataFim );
		} catch (ParseException e) {
			throw new DataIniInvalidaException();
		}
		
		if ( dataIni.after( dataFim ) )
			throw new DataFimAposDataIniException();
		
		List<Caixa> caixas = caixaRepository.filtra( dataIni, dataFim );
		List<CaixaResponse> responses = new ArrayList<>();
		for( Caixa c : caixas ) {
			CaixaResponse resp = caixaBuilder.novoCaixaResponse();
			caixaBuilder.carregaCaixaResponse( resp, c );
			
			responses.add( resp );
		}
		
		return responses;
	}
		
	public CaixaResponse buscaHojeCaixa( Long usuarioId ) 
			throws PerfilCaixaRequeridoException, 
				CaixaNaoAbertoException, 
				UsuarioNaoEncontradoException, 
				FuncionarioNaoEncontradoException {
				
		Caixa c = this.buscaHojeCaixaBean( usuarioId );
		
		CaixaResponse resp = caixaBuilder.novoCaixaResponse();
		caixaBuilder.carregaCaixaResponse( resp, c );
		
		return resp;					
	}
			
	public Caixa buscaHojeCaixaBean( Long usuarioId ) 
			throws PerfilCaixaRequeridoException, 
				CaixaNaoAbertoException,
				UsuarioNaoEncontradoException, 
				FuncionarioNaoEncontradoException {
		
		Funcionario f = this.buscaFuncionarioPorUID( usuarioId );
		
		Date hoje = dataUtil.apenasData( new Date() );
		
		Optional<Caixa> cop = caixaRepository.buscaCaixa( f.getId(), hoje );
		if ( !cop.isPresent() )
			throw new CaixaNaoAbertoException();
			
		return cop.get();		
	}
	
	public void deleteCaixa( Long caixaId ) throws CaixaNaoEncontradoException {
		if ( !caixaRepository.findById( caixaId ).isPresent() )
			throw new CaixaNaoEncontradoException();
		
		caixaRepository.deleteById( caixaId ); 
	}
	
	private Funcionario buscaFuncionarioPorUID( Long usuarioId )
			throws UsuarioNaoEncontradoException, 
				PerfilCaixaRequeridoException,
				FuncionarioNaoEncontradoException {
		
		Usuario u = usuarioRepository.findById( usuarioId ).orElseThrow( UsuarioNaoEncontradoException::new );
		
		if ( u.getTipo() != UsuarioTipo.CAIXA )
			throw new PerfilCaixaRequeridoException();
		
		Funcionario f = u.getFuncionario();
		if ( f == null )
			throw new FuncionarioNaoEncontradoException();
		
		return f;
	}
	
	
}
