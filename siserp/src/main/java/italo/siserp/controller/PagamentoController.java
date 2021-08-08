package italo.siserp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.siserp.util.enums_tipo.FormaPagTipoEnumConversor;

@RestController
@RequestMapping(value="/api/pagamento")
public class PagamentoController {
	
	@Autowired
	private FormaPagTipoEnumConversor enumConversor;

	@GetMapping(value="/formas")
	public ResponseEntity<Object> buscaTipos() {
		String[] tipos = enumConversor.getFormasPags();
		return ResponseEntity.ok( tipos );
	}

}
