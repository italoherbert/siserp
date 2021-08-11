package italo.siserp.builder;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.exception.DoubleInvalidoException;
import italo.siserp.exception.LancamentoTipoInvalidoException;
import italo.siserp.exception.LancamentoValorInvalidoException;
import italo.siserp.model.Caixa;
import italo.siserp.model.Lancamento;
import italo.siserp.model.LancamentoTipo;
import italo.siserp.model.request.SaveLancamentoRequest;
import italo.siserp.model.response.LancamentoResponse;
import italo.siserp.util.DataUtil;
import italo.siserp.util.LancamentoTipoEnumConversor;
import italo.siserp.util.NumeroUtil;

@Component
public class LancamentoBuilder {

	@Autowired
	private NumeroUtil numeroUtil;
	
	@Autowired
	private DataUtil dataUtil;
	
	@Autowired
	private LancamentoTipoEnumConversor lancTipoEnumConversor;
		
	public void carregaLancamento( Lancamento l, SaveLancamentoRequest request )
			throws LancamentoTipoInvalidoException, LancamentoValorInvalidoException {
		
		LancamentoTipo ltipo = lancTipoEnumConversor.getLancamentoTipo( request.getTipo() );
		if ( ltipo == null )
			throw new LancamentoTipoInvalidoException();
		
		l.setTipo( ltipo );
		l.setObs( request.getObs() );
		
		try {
			l.setValor( numeroUtil.stringParaDouble( request.getValor() ) );
		} catch (DoubleInvalidoException e) {
			throw new LancamentoValorInvalidoException();
		}
		
		l.setDataOperacao( new Date() );
	}
	
	public void carregaLancamentoResponse( LancamentoResponse resp, Lancamento l ) {
		resp.setId( l.getId() );
		resp.setTipo( lancTipoEnumConversor.getLancamentoTipoString( l.getTipo() ) );		
		resp.setValor( numeroUtil.doubleParaString( l.getValor() ) );
		resp.setDataOperacao( dataUtil.dataTimeParaString( l.getDataOperacao() ) );
		resp.setObs( l.getObs() ); 
	}
	
	public LancamentoResponse novoLancamentoResponse() {		
		LancamentoResponse resp = new LancamentoResponse();
		return resp;
	}
	
	public Lancamento novoLancamento() {
		return new Lancamento();
	}
			
	public Lancamento novoINILancamento( Caixa caixa ) {
		Lancamento lanc = new Lancamento();
		lanc.setCaixa( caixa );
		lanc.setDataOperacao( new Date() );
		return lanc;
	}
	
}



