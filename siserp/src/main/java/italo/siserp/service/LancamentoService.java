package italo.siserp.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.siserp.builder.LancamentoBuilder;
import italo.siserp.dao.CaixaDAO;
import italo.siserp.dao.bean.CaixaBalancoDAOTO;
import italo.siserp.exception.CaixaNaoAbertoException;
import italo.siserp.exception.CaixaNaoEncontradoException;
import italo.siserp.exception.FuncionarioNaoEncontradoException;
import italo.siserp.exception.LancamentoNaoEncontradoException;
import italo.siserp.exception.LancamentoTipoInvalidoException;
import italo.siserp.exception.LancamentoValorInvalidoException;
import italo.siserp.exception.PerfilCaixaRequeridoException;
import italo.siserp.exception.UsuarioNaoEncontradoException;
import italo.siserp.exception.ValorEmCaixaInsuficienteException;
import italo.siserp.model.Caixa;
import italo.siserp.model.Lancamento;
import italo.siserp.model.LancamentoTipo;
import italo.siserp.model.request.SaveLancamentoRequest;
import italo.siserp.model.response.LancamentoResponse;
import italo.siserp.repository.CaixaRepository;
import italo.siserp.repository.LancamentoRepository;

@Service
public class LancamentoService {

	@Autowired
	private CaixaRepository caixaRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private LancamentoBuilder lancamentoBuilder;

	@Autowired
	private CaixaDAO caixaDAO;
		
	@Transactional
	public void deletaLancamentosHoje( Long usuarioId ) 
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
				ValorEmCaixaInsuficienteException,
				UsuarioNaoEncontradoException, 
				FuncionarioNaoEncontradoException {
		
		Caixa c = caixaDAO.buscaHojeCaixaBean( usuarioId );
		CaixaBalancoDAOTO caixaBalancoDAOTO = caixaDAO.geraCaixaBalanco( c );
				
		Lancamento lanc = lancamentoBuilder.novoLancamento();
		lancamentoBuilder.carregaLancamento( lanc, request );
		
		double valor = lanc.getValor();
		if ( lanc.getTipo() == LancamentoTipo.DEBITO && valor > caixaBalancoDAOTO.getSaldo() )
			throw new ValorEmCaixaInsuficienteException();
		
		lanc.setCaixa( c );
		
		lancamentoRepository.save( lanc );
	}
			
	public List<LancamentoResponse> buscaLancamentosPorUsuarioId( Long usuarioId ) 
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
	
	public List<LancamentoResponse> buscaLancamentosPorCaixaId( Long caixaId ) throws CaixaNaoEncontradoException {
		Caixa c = caixaRepository.findById( caixaId ).orElseThrow( CaixaNaoEncontradoException::new );
		List<Lancamento> lancamentos = c.getLancamentos();
		
		List<LancamentoResponse> lista = new ArrayList<>();
		for( Lancamento l : lancamentos ) {
			LancamentoResponse resp = lancamentoBuilder.novoLancamentoResponse();
			lancamentoBuilder.carregaLancamentoResponse( resp, l );
			
			lista.add( resp );
		}
		return lista;
	}
		
	
	public void deleta( Long id ) throws LancamentoNaoEncontradoException {
		if ( !lancamentoRepository.existsById( id ) )
			throw new LancamentoNaoEncontradoException();
		
		lancamentoRepository.deleteById( id );
	}
	
}
