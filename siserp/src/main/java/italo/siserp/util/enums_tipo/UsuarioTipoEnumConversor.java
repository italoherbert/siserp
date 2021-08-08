package italo.siserp.util.enums_tipo;

import org.springframework.stereotype.Component;

import italo.siserp.model.UsuarioTipo;

@Component
public class UsuarioTipoEnumConversor {

	public String getUsuarioTipoString( UsuarioTipo tipo ) {
		switch( tipo ) {
			case ADMIN: return "ADMIN";
			case CAIXA: return "CAIXA";
			case GERENTE: return "GERENTE";			
		}
		return "DESCONHECIDO";
	}
	
	public UsuarioTipo getUsuarioTipo( String tipo ) {
		if ( tipo.equalsIgnoreCase( "ADMIN" ) ) {
			return UsuarioTipo.ADMIN;
		} else if ( tipo.equalsIgnoreCase( "CAIXA" ) ) {
			return UsuarioTipo.CAIXA;
		}  else if ( tipo.equalsIgnoreCase( "GERENTE" ) ) {
			return UsuarioTipo.GERENTE;
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
