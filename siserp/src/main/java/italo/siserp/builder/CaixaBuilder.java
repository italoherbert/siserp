package italo.siserp.builder;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.exception.CaixaValorInicialInvalidoException;
import italo.siserp.exception.DoubleInvalidoException;
import italo.siserp.model.Caixa;
import italo.siserp.model.request.AbreCaixaRequest;
import italo.siserp.model.response.CaixaResponse;
import italo.siserp.util.DataUtil;
import italo.siserp.util.NumeroUtil;

@Component
public class CaixaBuilder {

	@Autowired
	private FuncionarioBuilder funcionarioBuilder;
		
	@Autowired
	private NumeroUtil numeroUtil;
	
	@Autowired
	private DataUtil dataUtil;
	
	public void carregaCaixa( Caixa c, AbreCaixaRequest req, Date dataAbertura ) throws CaixaValorInicialInvalidoException {
		c.setDataAbertura( dataAbertura );
		try {
			c.setValorInicial( numeroUtil.stringParaDouble( req.getValorInicial() ) );
			c.setValor( c.getValorInicial() );
		} catch ( DoubleInvalidoException e ) {
			CaixaValorInicialInvalidoException ex = new CaixaValorInicialInvalidoException();
			ex.setParams( req.getValorInicial() ); 
			throw ex;
		}		
	}
	
	public void carregaCaixaResponse( CaixaResponse resp, Caixa c ) {
		resp.setId( c.getId() );
		resp.setDataAbertura( dataUtil.dataParaString( c.getDataAbertura() ) );
		resp.setValorInicial( numeroUtil.doubleParaString( c.getValorInicial() ) );
		resp.setValor( numeroUtil.doubleParaString( c.getValor() ) );
		
		funcionarioBuilder.carregaFuncionarioResponse( resp.getFuncionario(), c.getFuncionario() );
	}
	
	public CaixaResponse novoCaixaResponse() {		
		CaixaResponse resp = new CaixaResponse();
		return resp;
	}
	
	public Caixa novoCaixa() {
		return new Caixa();
	}
			
}


