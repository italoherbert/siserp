package italo.siserp.component.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.component.EnumConversor;
import italo.siserp.component.HashUtil;
import italo.siserp.model.Usuario;
import italo.siserp.model.request.SaveUsuarioRequest;
import italo.siserp.model.response.UsuarioResponse;

@Component
public class UsuarioBuilder {

	@Autowired
	private HashUtil hashUtil;
	
	@Autowired
	private EnumConversor enumConversor;
	
	public void carregaUsuario( Usuario u, SaveUsuarioRequest req ) {
		u.setUsername( req.getUsername() );
		u.setPassword( hashUtil.geraHash( req.getPassword() ) );
		u.setTipo( enumConversor.getUsuarioTipo( req.getTipo() ) ); 
	}
	
	public void carregaUsuarioResponse( UsuarioResponse resp, Usuario u ) {
		resp.setId( u.getId() );
		resp.setUsername( u.getUsername() );
		resp.setTipo( enumConversor.getUsuarioTipoString( u.getTipo() ) );
	}
	
	public UsuarioResponse novoUsuarioResponse() {
		return new UsuarioResponse();
	}
	
	public Usuario novoUsuario() {
		return new Usuario();
	}
	
}
