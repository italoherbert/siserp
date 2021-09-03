package italo.siserp.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.exception.PermissaoEscritaException;
import italo.siserp.exception.PermissaoLeituraException;
import italo.siserp.exception.PermissaoRemocaoException;
import italo.siserp.exception.PermissaoTipoInvalidoException;
import italo.siserp.model.PermissaoGrupo;
import italo.siserp.model.PermissaoTipo;
import italo.siserp.model.Recurso;
import italo.siserp.model.UsuarioGrupo;
import italo.siserp.model.request.SavePermissaoGrupoRequest;
import italo.siserp.model.request.SavePermissaoRequest;
import italo.siserp.model.response.PermissaoGrupoResponse;
import italo.siserp.util.PermissaoEnumConversor;

@Component
public class PermissaoGrupoBuilder {

	@Autowired
	private PermissaoEnumConversor permissaoEnumConversor;
	
	public void carregaPermissao( PermissaoGrupo pg, SavePermissaoRequest req ) 
			throws PermissaoLeituraException, 
				PermissaoEscritaException,
				PermissaoRemocaoException,
				PermissaoTipoInvalidoException {
		
		PermissaoTipo tipo = permissaoEnumConversor.getPermissaoTipo( req.getTipo() );
		if ( tipo == null )
			throw new PermissaoTipoInvalidoException();
		
		switch( tipo) {
			case LEITURA:
				if ( req.getValor().equals( "true" ) || req.getValor().equals( "false" ) )		
					pg.setLeitura( Boolean.parseBoolean( req.getValor() ) );
				else throw new PermissaoLeituraException();
				break;
			case ESCRITA:
				if ( req.getValor().equals( "true" ) || req.getValor().equals( "false" ) )		
					pg.setEscrita( Boolean.parseBoolean( req.getValor() ) );
				else throw new PermissaoLeituraException();
				break;
			case REMOCAO:
				if ( req.getValor().equals( "true" ) || req.getValor().equals( "false" ) )		
					pg.setRemocao( Boolean.parseBoolean( req.getValor() ) );
				else throw new PermissaoLeituraException();
				break;
		}
	}
		
	public void carregaPermissaoGrupo( PermissaoGrupo pg, SavePermissaoGrupoRequest req ) 
			throws PermissaoLeituraException, 
				PermissaoEscritaException,
				PermissaoRemocaoException {
				
		if ( req.getLeitura().equals( "true" ) || req.getLeitura().equals( "false" ) )		
			pg.setLeitura( Boolean.parseBoolean( req.getLeitura() ) );
		else throw new PermissaoLeituraException();
		
		if ( req.getEscrita().equals( "true" ) || req.getEscrita().equals( "false" ) )		
			pg.setEscrita( Boolean.parseBoolean( req.getEscrita() ) );
		else throw new PermissaoEscritaException();
		
		if ( req.getRemocao().equals( "true" ) || req.getRemocao().equals( "false" ) )		
			pg.setRemocao( Boolean.parseBoolean( req.getRemocao() ) );
		else throw new PermissaoRemocaoException();
	}
	
	public void carregaPermissaoGrupoResponse( PermissaoGrupoResponse resp, PermissaoGrupo pg ) {
		resp.setId( pg.getId() );
		resp.setRecurso( pg.getRecurso().getNome() );
			
		resp.setLeitura( String.valueOf( pg.isLeitura() ) );
		resp.setEscrita( String.valueOf( pg.isEscrita() ) );
		resp.setRemocao( String.valueOf( pg.isRemocao() ) );			
	}
	
	public PermissaoGrupoResponse novoPermissaoGrupoResponse() {
		return new PermissaoGrupoResponse();
	}
	
	public PermissaoGrupo novoPermissaoGrupo() {
		return new PermissaoGrupo();
	}
	
	public PermissaoGrupo novoINIPermissaoGrupo( UsuarioGrupo g, Recurso r ) {
		PermissaoGrupo pg = new PermissaoGrupo();
		pg.setGrupo( g ); 
		pg.setRecurso( r );
		pg.setLeitura( false );
		pg.setEscrita( false );
		pg.setRemocao( false );
		return pg;
	}

}
