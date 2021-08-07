package italo.siserp.builder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.exception.DataVendaInvalidaException;
import italo.siserp.exception.DebitoInvalidoException;
import italo.siserp.exception.DescontoInvalidoException;
import italo.siserp.exception.DoubleInvalidoException;
import italo.siserp.exception.PrecoUnitVendaInvalidoException;
import italo.siserp.exception.QuantidadeInvalidaException;
import italo.siserp.exception.SubtotalInvalidoException;
import italo.siserp.model.ItemVenda;
import italo.siserp.model.Venda;
import italo.siserp.model.request.SaveVendaRequest;
import italo.siserp.model.response.ItemVendaResponse;
import italo.siserp.model.response.VendaResponse;
import italo.siserp.util.DataUtil;
import italo.siserp.util.EnumConversor;
import italo.siserp.util.NumeroUtil;

@Component
public class VendaBuilder {


	@Autowired
	private ClienteBuilder clienteBuilder;
	
	@Autowired
	private ItemVendaBuilder itemVendaBuilder;
		
	@Autowired
	private DataUtil dataUtil;
	
	@Autowired
	private NumeroUtil numeroUtil;
	
	@Autowired
	private EnumConversor enumConversor;	
	
	public void carregaVenda( Venda v, SaveVendaRequest req ) 
			throws DataVendaInvalidaException, 
				PrecoUnitVendaInvalidoException,
				SubtotalInvalidoException,
				DescontoInvalidoException,
				DebitoInvalidoException,
				QuantidadeInvalidaException {
		try {
			v.setDataVenda( dataUtil.stringParaData( req.getDataVenda() ) );
		} catch (ParseException e) {
			DataVendaInvalidaException ex = new DataVendaInvalidaException();
			ex.setParams( req.getDataVenda() );
			throw ex;
		}
		
		try {
			v.setSubtotal( numeroUtil.stringParaDouble( req.getSubtotal() ) );
		} catch (DoubleInvalidoException e) {
			SubtotalInvalidoException ex = new SubtotalInvalidoException();
			ex.setParams( req.getSubtotal() );
			throw ex;
		}
		
		try {
			v.setDesconto( numeroUtil.stringParaDouble( req.getDesconto() ) );
		} catch (DoubleInvalidoException e) {
			DescontoInvalidoException ex = new DescontoInvalidoException();
			ex.setParams( req.getDesconto() );
			throw ex;
		}
				
		v.setFormaPag( enumConversor.getFormaPag( req.getFormaPag() ) ); 
	}
	
	public void carregaVendaResponse( VendaResponse resp, Venda v ) {
		resp.setId( v.getId() );
		resp.setDataVenda( dataUtil.dataParaString( v.getDataVenda() ) ); 
		resp.setSubtotal( numeroUtil.doubleParaString( v.getSubtotal() ) );
		resp.setDesconto( numeroUtil.doubleParaString( v.getDesconto() ) );
		resp.setDebito( numeroUtil.doubleParaString( v.getDebito() ) );
		resp.setModoPag( enumConversor.getFormaPagString( v.getFormaPag() ) ); 
		
		List<ItemVendaResponse> itemResps = new ArrayList<>();
		for( ItemVenda item : v.getItensVenda() ) {
			ItemVendaResponse itemResp = itemVendaBuilder.novoItemVendaResponse();
			itemVendaBuilder.carregaItemVendaResponse( itemResp, item );
			
			itemResps.add( itemResp );
		}
		resp.setItens( itemResps ); 				 				
	}	
	
	public VendaResponse novoVendaResponse() {		
		VendaResponse resp = new VendaResponse();
		resp.setCliente( clienteBuilder.novoClienteResponse() );
		return resp;
	}
	
	public Venda novoVenda() {
		return new Venda();
	}
	
	
	
}
