package italo.siserp.dao;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.exception.CaixaNaoAbertoException;
import italo.siserp.exception.FuncionarioNaoEncontradoException;
import italo.siserp.exception.PerfilCaixaRequeridoException;
import italo.siserp.exception.UsuarioNaoEncontradoException;
import italo.siserp.model.Caixa;
import italo.siserp.model.Funcionario;
import italo.siserp.model.Usuario;
import italo.siserp.model.UsuarioTipo;
import italo.siserp.repository.CaixaRepository;
import italo.siserp.repository.UsuarioRepository;
import italo.siserp.util.DataUtil;

@Component
public class CaixaDAO {
	
	@Autowired
	private CaixaRepository caixaRepository;
			
	@Autowired
	private UsuarioRepository usuarioRepository;
		
	@Autowired
	private DataUtil dataUtil;
			
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
	
	public Funcionario buscaFuncionarioPorUID( Long usuarioId )
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