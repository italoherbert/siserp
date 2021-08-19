package italo.siserp.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.dao.bean.CaixaBalancoDAOTO;
import italo.siserp.exception.CaixaNaoAbertoException;
import italo.siserp.exception.FuncionarioNaoEncontradoException;
import italo.siserp.exception.PerfilCaixaRequeridoException;
import italo.siserp.exception.UsuarioNaoEncontradoException;
import italo.siserp.model.Caixa;
import italo.siserp.model.FormaPag;
import italo.siserp.model.Funcionario;
import italo.siserp.model.Lancamento;
import italo.siserp.model.LancamentoTipo;
import italo.siserp.model.Usuario;
import italo.siserp.model.UsuarioGrupo;
import italo.siserp.model.Venda;
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
		
	public CaixaBalancoDAOTO geraCaixaBalanco( Caixa c ) {
		
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
		double vendasAPrazoTotal = 0;
		List<Venda> vendas = c.getVendas();
		for( Venda v : vendas ) {
			double total = v.getSubtotal() * (1.0d - v.getDesconto() );
			if ( v.getFormaPag() == FormaPag.CARTAO )
				cartaoValorRecebido += total;			
			if ( v.getFormaPag() == FormaPag.DEBITO ) 
				vendasAPrazoTotal += total;
		}
		
		CaixaBalancoDAOTO balanco = new CaixaBalancoDAOTO();
		balanco.setFuncionarioNome( c.getFuncionario().getPessoa().getNome() ); 
		balanco.setDataAbertura( dataAbertura );
		balanco.setDebito( debito );
		balanco.setCredito( credito );
		balanco.setSaldo( saldo ); 
		balanco.setCartaoValorRecebido( cartaoValorRecebido );
		balanco.setVendasAPrazoTotal( vendasAPrazoTotal );
		return balanco;
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
	
	public Funcionario buscaFuncionarioPorUID( Long usuarioId )
			throws UsuarioNaoEncontradoException, 
				PerfilCaixaRequeridoException,
				FuncionarioNaoEncontradoException {
		
		Usuario u = usuarioRepository.findById( usuarioId ).orElseThrow( UsuarioNaoEncontradoException::new );
		
		if ( !u.getGrupo().getNome().equalsIgnoreCase( UsuarioGrupo.CAIXA ) )
			throw new PerfilCaixaRequeridoException();
		
		Funcionario f = u.getFuncionario();
		if ( f == null )
			throw new FuncionarioNaoEncontradoException();
		
		return f;
	}
	
}