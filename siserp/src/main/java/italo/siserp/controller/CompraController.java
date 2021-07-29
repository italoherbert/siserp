package italo.siserp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.siserp.exception.DataCompraException;
import italo.siserp.exception.DataPagamentoInvalidaException;
import italo.siserp.exception.DataVencimentoInvalidaException;
import italo.siserp.exception.ParcelaValorInvalidoException;
import italo.siserp.exception.PrecoUnitCompraInvalidoException;
import italo.siserp.exception.PrecoUnitVendaInvalidoException;
import italo.siserp.exception.QuantidadeInvalidaException;
import italo.siserp.model.request.SaveCompraRequest;
import italo.siserp.model.response.ErroResponse;
import italo.siserp.service.CompraService;

@RestController
@RequestMapping(value = "/api/compra")
public class CompraController {

	@Autowired
	private CompraService compraService;
	
	@PostMapping(value="/registra")
	public ResponseEntity<Object> registraCompra( @RequestBody SaveCompraRequest request ) {
		try {
			compraService.salvaCompra( request );
			return ResponseEntity.ok().build();
		} catch (DataCompraException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_COMPRA_INVALIDA, e.getParams() ) );
		} catch (PrecoUnitCompraInvalidoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_PRECO_UNIT_COMPRA_INVALIDO, e.getParams() ) );			
		} catch (PrecoUnitVendaInvalidoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PRODUTO_PRECO_UNIT_VENDA_INVALIDO, e.getParams() ) );			
		} catch (QuantidadeInvalidaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.QUANTIDADE_INVALIDA, e.getParams() ) );			
		} catch (ParcelaValorInvalidoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PARCELA_VALOR_INVALIDO, e.getParams() ) );			
		} catch (DataPagamentoInvalidaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_PAGAMENTO_INVALIDA, e.getParams() ) );						
		} catch (DataVencimentoInvalidaException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.DATA_VENCIMENTO_INVALIDA, e.getParams() ) );						
		}
	}
	
	public ResponseEntity<Object> buscaCompras( String dataIni, String dataFim ) {
		
	}
	
}
