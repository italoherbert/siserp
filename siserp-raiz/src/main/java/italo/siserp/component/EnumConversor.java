package italo.siserp.component;

import org.springframework.stereotype.Component;

import italo.siserp.model.UsuarioTipo;

@Component
public class EnumConversor {

	public String getUsuarioTipoString( UsuarioTipo tipo ) {
		switch( tipo ) {
			case RAIZ: return "RAIZ";
			case ADMIN: return "ADMIN";		
		}
		return "DESCONHECIDO";
	}
	
	public UsuarioTipo getUsuarioTipo( String tipo ) {
		if ( tipo.equalsIgnoreCase( "RAIZ" ) ) {
			return UsuarioTipo.RAIZ;
		} else if ( tipo.equalsIgnoreCase( "ADMIN" ) ) {
			return UsuarioTipo.ADMIN;
		}
		return null;
	}
	
	public String[] getUsuarioTipos() {
		UsuarioTipo[] valores = UsuarioTipo.values();
		String[] tipos = new String[ valores.length ];
		for( int i = 0; i < valores.length; i++ )
			tipos[ i ] = String.valueOf( valores[ i ] );
		return tipos;
	}
	
}
