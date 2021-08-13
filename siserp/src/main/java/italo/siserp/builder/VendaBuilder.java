package italo.siserp.builder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.exception.DataVendaInvalidaException;
import italo.siserp.exception.DebitoInvalidoException;
import italo.siserp.exception.DescontoInvalidoException;
import italo.siserp.exception.DoubleInvalidoException;
import italo.siserp.exception.FormaPagInvalidaException;
import italo.siserp.exception.PrecoUnitVendaInvalidoException;
import italo.siserp.exception.QuantidadeInvalidaException;
import italo.siserp.exception.SubtotalInvalidoException;
import italo.siserp.model.FormaPag;
import italo.siserp.model.ItemVenda;
import italo.siserp.model.Venda;
import italo.siserp.model.request.SaveVendaRequest;
import italo.siserp.model.response.ItemVendaResponse;
import italo.siserp.model.response.VendaResponse;
import italo.siserp.util.DataUtil;
import italo.siserp.util.FormaPagEnumConversor;
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
	private FormaPagEnumConversor enumConversor;	
	
	public void carregaVenda( Venda v, SaveVendaRequest req ) 
			throws DataVendaInvalidaException, 
				PrecoUnitVendaInvalidoException,
				SubtotalInvalidoException,
				DescontoInvalidoException,
				DebitoInvalidoException,
				QuantidadeInvalidaException,
				FormaPagInvalidaException {
		
		v.setDataVenda( dataUtil.apenasData( new Date() ) );
				
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
		
		FormaPag formaPag =  enumConversor.getFormaPag( req.getFormaPag() );
		if ( formaPag == null )
			throw new FormaPagInvalidaException();
				
		v.setFormaPag( formaPag ); 
	}
	
	public void carregaVendaResponse( VendaResponse resp, Venda v ) {
		resp.setId( v.getId() );
		resp.setDataVenda( dataUtil.dataParaString( v.getDataVenda() ) ); 
		resp.setSubtotal( numeroUtil.doubleParaString( v.getSubtotal() ) );
		resp.setDesconto( numeroUtil.doubleParaString( v.getDesconto() ) );
		resp.setDebito( numeroUtil.doubleParaString( v.getDebito() ) );
		resp.setFormaPag( enumConversor.getFormaPagString( v.getFormaPag() ) ); 
		
		List<ItemVendaResponse> itemResps = new ArrayList<>();
		List<ItemVenda> itens = v.getItensVenda();
		for( ItemVenda item : itens ) {
			ItemVendaResponse itemResp = itemVendaBuilder.novoItemVendaResponse();
			itemVendaBuilder.carregaItemVendaResponse( itemResp, item );
			
			itemResps.add( itemResp );
		}
		resp.setItens( itemResps );
		
		if ( v.getCliente() != null )
			clienteBuilder.carregaClienteResponse( resp.getCliente(), v.getCliente() ); 
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
