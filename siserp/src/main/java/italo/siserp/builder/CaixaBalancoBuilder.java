package italo.siserp.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.dao.bean.CaixaBalancoDAOTO;
import italo.siserp.service.response.CaixaBalancoResponse;
import italo.siserp.util.DataUtil;
import italo.siserp.util.NumeroUtil;

@Component
public class CaixaBalancoBuilder {

	@Autowired
	private NumeroUtil numeroUtil;
	
	@Autowired
	private DataUtil dataUtil;
	
	public void carregaCaixaBalancoResponse( CaixaBalancoResponse resp, CaixaBalancoDAOTO cb ) {
		resp.setFuncionarioNome( cb.getFuncionarioNome() );
		resp.setDataAbertura( dataUtil.dataParaString( cb.getDataAbertura() ) );
		resp.setDebito( numeroUtil.doubleParaString( cb.getDebito() ) );
		resp.setCredito( numeroUtil.doubleParaString( cb.getCredito() ) );
		resp.setSaldo( numeroUtil.doubleParaString( cb.getSaldo() ) );
		resp.setCartaoValorRecebido( numeroUtil.doubleParaString( cb.getCartaoValorRecebido() ) );
		resp.setTotalVendasAPrazo( numeroUtil.doubleParaString( cb.getVendasAPrazoTotal() ) ); 
	}
	
	public CaixaBalancoResponse novoCaixaBalancoResponse() {
		return new CaixaBalancoResponse();
	}
		
}
