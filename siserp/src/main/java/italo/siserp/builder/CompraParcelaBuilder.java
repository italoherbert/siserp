package italo.siserp.builder;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.exception.DataPagamentoInvalidaException;
import italo.siserp.exception.DataVencimentoInvalidaException;
import italo.siserp.exception.ParcelaValorInvalidoException;
import italo.siserp.model.Compra;
import italo.siserp.model.CompraParcela;
import italo.siserp.model.request.SaveCompraParcelaRequest;
import italo.siserp.model.response.CompraParcelaResponse;
import italo.siserp.util.DataUtil;
import italo.siserp.util.NumeroUtil;

@Component
public class CompraParcelaBuilder {
	
	@Autowired
	private DataUtil dataUtil;
	
	@Autowired
	private NumeroUtil numeroUtil;
	
	public void carregaCompraParcela( CompraParcela parcela, SaveCompraParcelaRequest req ) 
			throws ParcelaValorInvalidoException, 
				DataPagamentoInvalidaException, 
				DataVencimentoInvalidaException {
		try {
			parcela.setValor( numeroUtil.stringParaDouble( req.getValor() ) );
		} catch ( ParseException e ) {
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
	
	public void carregaCompraParcelaResponse( CompraParcelaResponse resp, CompraParcela parcela ) {
		resp.setId( parcela.getId() );		
		resp.setValor( String.valueOf( parcela.getValor() ) );
		resp.setDataPagamento( dataUtil.dataParaString( parcela.getDataPagamento() ) );
		resp.setDataVencimento( dataUtil.dataParaString( parcela.getDataVencimento() ) );		
	}	
	
	public CompraParcelaResponse novoCompraParcelaResponse() {
		return new CompraParcelaResponse();
	}
	
	public CompraParcela novoCompraParcela( Long compraId ) {
		CompraParcela parcela = new CompraParcela();
		parcela.setCompra( new Compra() );
		
		parcela.getCompra().setId( compraId );		
		return parcela;
	}
	
	
}
