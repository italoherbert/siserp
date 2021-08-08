package italo.siserp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.siserp.util.enums_tipo.UsuarioTipoEnumConversor;

@RestController
@RequestMapping(value="/api/usuario")
public class UsuarioController {
	
	@Autowired
	private UsuarioTipoEnumConversor usuarioTipoEnumConversor;

	@GetMapping(value="/tipos")
	public ResponseEntity<Object> buscaTipos() {
		String[] tipos = usuarioTipoEnumConversor.getUsuarioTipos();
		return ResponseEntity.ok( tipos );
	}

}
