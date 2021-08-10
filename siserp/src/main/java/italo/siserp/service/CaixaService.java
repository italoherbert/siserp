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
import italo.siserp.model.request.AbreCaixaRequest;
import italo.siserp.model.request.BuscaCaixasRequest;
import italo.siserp.model.response.CaixaResponse;
import italo.siserp.repository.CaixaRepository;
import italo.siserp.util.DataUtil;

@Service
public class CaixaService {
	
	@Autowired
	private CaixaRepository caixaRepository;
				
	@Autowired
	private CaixaBuilder caixaBuilder;
	
	@Autowired
	private LancamentoBuilder lancamentoBuilder;
		
	@Autowired
	private CaixaDAO caixaDAO;
	
	@Autowired
	private DataUtil dataUtil;
					
	public void abreGetCaixaSeNaoAberto( Long usuarioId, AbreCaixaRequest req ) 
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
	
	public List<CaixaResponse> filtra( BuscaCaixasRequest request ) 
			throws DataIniInvalidaException, 
				DataFimInvalidaException, 
				DataFimAposDataIniException {
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
			throw new DataFimAposDataIniException();
		
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
