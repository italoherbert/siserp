package italo.siserp.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.model.ItemVenda;
import italo.siserp.service.response.ItemVendaResponse;
import italo.siserp.util.NumeroUtil;

@Component
public class ItemVendaBuilder {

	@Autowired
	private ProdutoBuilder produtoBuilder;
	
	@Autowired
	private NumeroUtil numeroUtil;
				
	public void carregaItemVendaResponse( ItemVendaResponse resp, ItemVenda item ) {
		resp.setId( item.getId() ); 
		resp.setPrecoUnitario( numeroUtil.doubleParaString( item.getPrecoUnitario() ) );
		resp.setQuantidade( numeroUtil.doubleParaString( item.getQuantidade() ) );
		
		produtoBuilder.carregaProdutoResponse( resp.getProduto(), item.getProduto() ); 
	}	
	
	public ItemVendaResponse novoItemVendaResponse() {
		ItemVendaResponse resp = new ItemVendaResponse();
		resp.setProduto( produtoBuilder.novoProdutoResponse() );
		return resp;
	}
	
	public ItemVenda novoItemVenda() {
		return new ItemVenda();		
	}
	
}
