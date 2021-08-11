package italo.siserp.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.model.PermissaoGrupo;
import italo.siserp.model.UsuarioGrupo;
import italo.siserp.model.request.SaveUsuarioGrupoRequest;
import italo.siserp.model.response.PermissaoGrupoResponse;
import italo.siserp.model.response.UsuarioGrupoResponse;

@Component
public class UsuarioGrupoBuilder {
		
	@Autowired
	private PermissaoGrupoBuilder permissaoGrupoBuilder;
	
	public void carregaUsuarioGrupo( UsuarioGrupo g, SaveUsuarioGrupoRequest req ) {
		g.setNome( req.getNome() );			
	}
	
	public void carregaUsuarioGrupoResponse( UsuarioGrupoResponse resp, UsuarioGrupo g ) {
		resp.setId( g.getId() );
		resp.setNome( g.getNome() ); 
		
		List<PermissaoGrupoResponse> grupos = new ArrayList<>();
		List<PermissaoGrupo> permissaoGrupos = g.getPermissaoGrupos();
		for( PermissaoGrupo pg : permissaoGrupos ) {			
			PermissaoGrupoResponse grupo = permissaoGrupoBuilder.novoPermissaoGrupoResponse();
			permissaoGrupoBuilder.carregaPermissaoGrupoResponse( grupo, pg );
			
			grupos.add( grupo );
		}
		resp.setPermissaoGrupos( grupos );
	}
	
	public UsuarioGrupoResponse novoUsuarioGrupoResponse() {
		return new UsuarioGrupoResponse();
	}
	
	public UsuarioGrupo novoUsuarioGrupo() {
		return new UsuarioGrupo();
	}
	
}
