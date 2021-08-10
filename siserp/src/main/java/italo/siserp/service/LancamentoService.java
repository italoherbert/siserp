package italo.siserp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.siserp.builder.LancamentoBuilder;
import italo.siserp.dao.CaixaDAO;
import italo.siserp.exception.CaixaNaoAbertoException;
import italo.siserp.exception.FuncionarioNaoEncontradoException;
import italo.siserp.exception.LancamentoNaoEncontradoException;
import italo.siserp.exception.LancamentoTipoInvalidoException;
import italo.siserp.exception.LancamentoValorInvalidoException;
import italo.siserp.exception.PerfilCaixaRequeridoException;
import italo.siserp.exception.UsuarioNaoEncontradoException;
import italo.siserp.model.Caixa;
import italo.siserp.model.Lancamento;
import italo.siserp.model.LancamentoTipo;
import italo.siserp.model.request.SaveLancamentoRequest;
import italo.siserp.model.response.CaixaBalancoResponse;
import italo.siserp.model.response.LancamentoResponse;
import italo.siserp.repository.LancamentoRepository;
import italo.siserp.util.DataUtil;
import italo.siserp.util.NumeroUtil;

@Service
public class LancamentoService {

	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private LancamentoBuilder lancamentoBuilder;

	@Autowired
	private CaixaDAO caixaDAO;
	
	@Autowired
	private DataUtil dataUtil;
	
	@Autowired
	private NumeroUtil numeroUtil;
		
	@Transactional
	public void deletaLancamentos( Long usuarioId ) 
			throws PerfilCaixaRequeridoException, 
				CaixaNaoAbertoException, 
				UsuarioNaoEncontradoException,
				FuncionarioNaoEncontradoException {
				
		Caixa c = caixaDAO.buscaHojeCaixaBean( usuarioId );
		
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
		
		Caixa c = caixaDAO.buscaHojeCaixaBean( usuarioId );
		
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
		
		Caixa c = caixaDAO.buscaHojeCaixaBean( usuarioId );
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
	
	public List<LancamentoResponse> buscaLancamentos( Long usuarioId ) 
			throws PerfilCaixaRequeridoException, 
				CaixaNaoAbertoException, 
				UsuarioNaoEncontradoException,
				FuncionarioNaoEncontradoException {
		
		Caixa c = caixaDAO.buscaHojeCaixaBean( usuarioId );
		List<Lancamento> lancamentos = c.getLancamentos();
		
		List<LancamentoResponse> responses = new ArrayList<>();
		for( Lancamento l : lancamentos ) {
			LancamentoResponse resp = lancamentoBuilder.novoLancamentoResponse();
			lancamentoBuilder.carregaLancamentoResponse( resp, l );
			
			responses.add( resp );
		}
		
		return responses;
	}
	
	public void deleta( Long id ) throws LancamentoNaoEncontradoException {
		if ( !lancamentoRepository.existsById( id ) )
			throw new LancamentoNaoEncontradoException();
		
		lancamentoRepository.deleteById( id );
	}
	
}
