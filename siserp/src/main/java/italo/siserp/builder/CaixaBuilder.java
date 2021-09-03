package italo.siserp.builder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.model.Caixa;
import italo.siserp.model.Lancamento;
import italo.siserp.model.request.AbreCaixaRequest;
import italo.siserp.model.response.CaixaResponse;
import italo.siserp.model.response.LancamentoResponse;
import italo.siserp.util.DataUtil;

@Component
public class CaixaBuilder {

	@Autowired
	private FuncionarioBuilder funcionarioBuilder;
	
	@Autowired
	private LancamentoBuilder lancamentoBuilder;
		
	@Autowired
	private DataUtil dataUtil;
			
	public void carregaCaixa( Caixa c, AbreCaixaRequest request ) {
		c.setDataAbertura( dataUtil.apenasData( new Date() ) );
	}
	
	public void carregaCaixaResponse( CaixaResponse resp, Caixa c ) {
		resp.setId( c.getId() );
		resp.setDataAbertura( dataUtil.dataParaString( c.getDataAbertura() ) );
		
		List<LancamentoResponse> lancamentoResponses = new ArrayList<>();
		List<Lancamento> lancamentos = c.getLancamentos();
		for( Lancamento l : lancamentos ) {
			LancamentoResponse lancResp = lancamentoBuilder.novoLancamentoResponse();
			lancamentoBuilder.carregaLancamentoResponse( lancResp, l );
			
			lancamentoResponses.add( lancResp );
		}
		
		resp.setLancamentos( lancamentoResponses );
		
		funcionarioBuilder.carregaFuncionarioResponse( resp.getFuncionario(), c.getFuncionario() );
	}
	
	public CaixaResponse novoCaixaResponse() {		
		CaixaResponse resp = new CaixaResponse();
		resp.setFuncionario( funcionarioBuilder.novoFuncionarioResponse() ); 
		return resp;
	}
	
	public Caixa novoCaixa() {
		return new Caixa();
	}
			
}


