package italo.siserp.builder;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.exception.DataPagamentoInvalidaException;
import italo.siserp.exception.DataVencimentoInvalidaException;
import italo.siserp.exception.DoubleInvalidoException;
import italo.siserp.exception.ParcelaValorInvalidoException;
import italo.siserp.model.Venda;
import italo.siserp.model.VendaParcela;
import italo.siserp.service.request.SaveVendaParcelaRequest;
import italo.siserp.service.response.VendaParcelaResponse;
import italo.siserp.util.DataUtil;
import italo.siserp.util.NumeroUtil;

@Component
public class VendaParcelaBuilder {
	
	@Autowired
	private DataUtil dataUtil;
	
	@Autowired
	private NumeroUtil numeroUtil;
		
	public void carregaVendaParcela( VendaParcela parcela, SaveVendaParcelaRequest req ) 
			throws ParcelaValorInvalidoException, 
				DataPagamentoInvalidaException, 
				DataVencimentoInvalidaException {
		try {
			parcela.setValor( numeroUtil.stringParaDouble( req.getValor() ) );
		} catch ( DoubleInvalidoException e ) {
			ParcelaValorInvalidoException ex = new ParcelaValorInvalidoException();
			ex.setParams( req.getValor() ); 
			throw ex;
		}
		
		try {
			parcela.setDebito( numeroUtil.stringParaDouble( req.getValor() ) );
		} catch ( DoubleInvalidoException e ) {
			ParcelaValorInvalidoException ex = new ParcelaValorInvalidoException();
			ex.setParams( req.getValor() ); 
			throw ex;
		}
		
		try {
			parcela.setDataPagamento( dataUtil.stringParaData( req.getDataPagamento() ) );
		} catch (ParseException e) {
			DataPagamentoInvalidaException ex = new DataPagamentoInvalidaException();
			ex.setParams( req.getDataPagamento() );
			throw ex;
		}
		
		try {
			parcela.setDataVencimento( dataUtil.stringParaData( req.getDataVencimento() ) );
		} catch (ParseException e) {
			DataVencimentoInvalidaException ex = new DataVencimentoInvalidaException();
			ex.setParams( req.getDataPagamento() );
			throw ex;
		}				
	}
	
	public void carregaVendaParcelaResponse( VendaParcelaResponse resp, VendaParcela parcela ) {
		resp.setId( parcela.getId() );		
		resp.setValor( numeroUtil.doubleParaString( parcela.getValor() ) );
		resp.setDebito( numeroUtil.doubleParaString( parcela.getDebito() ) );
		resp.setDataPagamento( dataUtil.dataParaString( parcela.getDataPagamento() ) );
		resp.setDataVencimento( dataUtil.dataParaString( parcela.getDataVencimento() ) );
	}	
	
	public VendaParcelaResponse novoVendaParcelaResponse() {
		return new VendaParcelaResponse();
	}
	
	public VendaParcela novoVendaParcela( Long vendaId ) {
		VendaParcela parcela = new VendaParcela();
		parcela.setVenda( new Venda() );
		
		parcela.getVenda().setId( vendaId );		
		return parcela;
	}
	
	
}
