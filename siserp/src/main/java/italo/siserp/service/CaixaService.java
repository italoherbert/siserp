package italo.siserp.service;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.siserp.builder.CaixaBuilder;
import italo.siserp.exception.CaixaJaAbertoException;
import italo.siserp.exception.CaixaNaoAbertoException;
import italo.siserp.exception.CaixaValorInicialInvalidoException;
import italo.siserp.exception.FuncionarioNaoEncontradoException;
import italo.siserp.exception.PerfilCaixaRequeridoException;
import italo.siserp.exception.UsuarioNaoEncontradoException;
import italo.siserp.model.Caixa;
import italo.siserp.model.Funcionario;
import italo.siserp.model.Usuario;
import italo.siserp.model.UsuarioTipo;
import italo.siserp.model.request.AbreCaixaRequest;
import italo.siserp.model.response.CaixaResponse;
import italo.siserp.repository.CaixaRepository;
import italo.siserp.repository.UsuarioRepository;
import italo.siserp.util.DataUtil;

@Service
public class CaixaService {
	
	@Autowired
	private CaixaRepository caixaRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private CaixaBuilder caixaBuilder;
	
	@Autowired
	private DataUtil dataUtil;
	
	public void abreGetCaixaSeNaoAberto( Long usuarioId, AbreCaixaRequest req ) 
			throws PerfilCaixaRequeridoException,
				CaixaValorInicialInvalidoException,
				CaixaJaAbertoException {
		
		Funcionario f;
		try {
			f = this.buscaFuncionarioPorUID( usuarioId );
		} catch (UsuarioNaoEncontradoException | FuncionarioNaoEncontradoException e) {
			throw new PerfilCaixaRequeridoException();
		}
		
		Date hoje = dataUtil.apenasData( new Date() );
		
		Optional<Caixa> cop = caixaRepository.buscaCaixa( f.getId(), hoje );
		if ( cop.isPresent() )
			throw new CaixaJaAbertoException();
		
		Caixa c = caixaBuilder.novoCaixa();
		caixaBuilder.carregaCaixa( c, req, hoje );
		
		c.setFuncionario( f );
		f.setCaixas( Arrays.asList( c ) );
		
		caixaRepository.save( c );		
	}
	
	public CaixaResponse buscaPorUsuarioID( Long usuarioId ) 
			throws PerfilCaixaRequeridoException, CaixaNaoAbertoException {
		
		try {
			Funcionario f = this.buscaFuncionarioPorUID( usuarioId );
			if ( f == null )
				throw new PerfilCaixaRequeridoException();
			
			return this.buscaPorFuncionarioID( f.getId() );	
		} catch (UsuarioNaoEncontradoException | FuncionarioNaoEncontradoException e) {
			throw new PerfilCaixaRequeridoException();
		}
		
		
	}
	
	public CaixaResponse buscaPorFuncionarioID( Long funcionarioId ) 
			throws PerfilCaixaRequeridoException, CaixaNaoAbertoException {
		
		Caixa c = this.buscaHojeCaixa( funcionarioId );
		
		CaixaResponse resp = caixaBuilder.novoCaixaResponse();
		caixaBuilder.carregaCaixaResponse( resp, c );
		
		return resp;
	}
		
	public Caixa buscaHojeCaixa( Long usuarioId ) 
			throws PerfilCaixaRequeridoException, CaixaNaoAbertoException {
		try {
			Funcionario f = this.buscaFuncionarioPorUID( usuarioId );
			
			Date hoje = dataUtil.apenasData( new Date() );
			
			Optional<Caixa> cop = caixaRepository.buscaCaixa( f.getId(), hoje );
			if ( !cop.isPresent() )
				throw new CaixaNaoAbertoException();
				
			return cop.get();
		} catch (UsuarioNaoEncontradoException | FuncionarioNaoEncontradoException e) {
			throw new PerfilCaixaRequeridoException();
		}
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
