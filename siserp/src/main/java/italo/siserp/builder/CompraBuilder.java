package italo.siserp.builder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.exception.DataCompraException;
import italo.siserp.exception.PrecoUnitCompraInvalidoException;
import italo.siserp.exception.PrecoUnitVendaInvalidoException;
import italo.siserp.exception.QuantidadeInvalidaException;
import italo.siserp.model.Compra;
import italo.siserp.model.CompraParcela;
import italo.siserp.model.ItemCompra;
import italo.siserp.model.request.SaveCompraRequest;
import italo.siserp.model.response.CompraParcelaResponse;
import italo.siserp.model.response.CompraResponse;
import italo.siserp.model.response.FornecedorResponse;
import italo.siserp.model.response.ItemCompraResponse;
import italo.siserp.util.DataUtil;

@Component
public class CompraBuilder {

	@Autowired
	private CompraParcelaBuilder compraParcelaBuilder;
	
	@Autowired
	private ItemCompraBuilder itemCompraBuilder;
	
	@Autowired
	private FornecedorBuilder fornecedorBuilder;
	
	@Autowired
	private DataUtil dataUtil;
	
	public void carregaCompra( Compra c, SaveCompraRequest req ) 
			throws DataCompraException, 
				PrecoUnitCompraInvalidoException,
				PrecoUnitVendaInvalidoException,
				QuantidadeInvalidaException {
		try {
			c.setDataCompra( dataUtil.stringParaData( req.getDataCompra() ) );
		} catch (ParseException e) {
			DataCompraException ex = new DataCompraException();
			ex.setParams( req.getDataCompra() );
			throw ex;
		}						
	}
	
	public void carregaCompraResponse( CompraResponse resp, Compra c ) {
		resp.setId( c.getId() );
		resp.setDataCompra( dataUtil.dataParaString( c.getDataCompra() ) ); 

		fornecedorBuilder.carregaFornecedorResponse( resp.getFornecedor(), c.getFornecedor() ); 
		
		List<ItemCompraResponse> itemResps = new ArrayList<>();
		for( ItemCompra item : c.getItensCompra() ) {
			ItemCompraResponse itemResp = itemCompraBuilder.novoItemCompraResponse();
			itemCompraBuilder.carregaItemCompraResponse( itemResp, item );
			
			itemResps.add( itemResp );
		}
		resp.setItens( itemResps ); 
		
		List<CompraParcelaResponse> parcelaResps = new ArrayList<>();
		for( CompraParcela parcela : c.getParcelas() ) {
			CompraParcelaResponse parcelaResp = compraParcelaBuilder.novoCompraParcelaResponse();
			compraParcelaBuilder.carregaCompraParcelaResponse( parcelaResp, parcela );
			
			parcelaResps.add( parcelaResp );
		}
		resp.setParcelas( parcelaResps ); 				
	}	
	
	public CompraResponse novoCompraResponse() {		
		CompraResponse resp = new CompraResponse();
		resp.setFornecedor( new FornecedorResponse() );
		return resp;
	}
	
	public Compra novoCompra() {
		return new Compra();
	}
	
	
}
