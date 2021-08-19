package italo.siserp.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.model.Usuario;
import italo.siserp.service.request.SaveUsuarioRequest;
import italo.siserp.service.response.UsuarioResponse;
import italo.siserp.util.HashUtil;

@Component
public class UsuarioBuilder {

	@Autowired
	private UsuarioGrupoBuilder usuarioGrupoBuilder;
	
	@Autowired
	private HashUtil hashUtil;
		
	public void carregaUsuario( Usuario u, SaveUsuarioRequest req ) {
		u.setUsername( req.getUsername() );
		u.setPassword( hashUtil.geraHash( req.getPassword() ) );
		
		usuarioGrupoBuilder.carregaUsuarioGrupo( u.getGrupo(), req.getGrupo() );
	}
	
	public void carregaUsuarioResponse( UsuarioResponse resp, Usuario u ) {
		resp.setId( u.getId() );
		resp.setUsername( u.getUsername() );
		
		usuarioGrupoBuilder.carregaUsuarioGrupoResponse( resp.getGrupo(), u.getGrupo() ); 
	}
	
	public UsuarioResponse novoUsuarioResponse() {
		UsuarioResponse resp = new UsuarioResponse();
		resp.setGrupo( usuarioGrupoBuilder.novoUsuarioGrupoResponse() );
		return resp;
	}
	
	public Usuario novoUsuario() {
		Usuario u = new Usuario();
		u.setGrupo( usuarioGrupoBuilder.novoUsuarioGrupo() );		
		return u;
	}
	
}
