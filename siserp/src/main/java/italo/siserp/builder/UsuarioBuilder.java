package italo.siserp.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.exception.UsuarioTipoInvalidoException;
import italo.siserp.model.Usuario;
import italo.siserp.model.UsuarioTipo;
import italo.siserp.model.request.SaveUsuarioRequest;
import italo.siserp.model.response.UsuarioResponse;
import italo.siserp.util.HashUtil;
import italo.siserp.util.enums_tipo.UsuarioTipoEnumConversor;

@Component
public class UsuarioBuilder {

	@Autowired
	private HashUtil hashUtil;
	
	@Autowired
	private UsuarioTipoEnumConversor usuarioTipoEnumConversor;
	
	public void carregaUsuario( Usuario u, SaveUsuarioRequest req ) throws UsuarioTipoInvalidoException {
		u.setUsername( req.getUsername() );
		u.setPassword( hashUtil.geraHash( req.getPassword() ) );
		
		UsuarioTipo tipo = usuarioTipoEnumConversor.getUsuarioTipo( req.getTipo() );
		if ( tipo == null )
			throw new UsuarioTipoInvalidoException();
		
		u.setTipo( usuarioTipoEnumConversor.getUsuarioTipo( req.getTipo() ) ); 
	}
	
	public void carregaUsuarioResponse( UsuarioResponse resp, Usuario u ) {
		resp.setId( u.getId() );
		resp.setUsername( u.getUsername() );
		resp.setTipo( usuarioTipoEnumConversor.getUsuarioTipoString( u.getTipo() ) );
	}
	
	public UsuarioResponse novoUsuarioResponse() {
		return new UsuarioResponse();
	}
	
	public Usuario novoUsuario() {
		return new Usuario();
	}
	
}
