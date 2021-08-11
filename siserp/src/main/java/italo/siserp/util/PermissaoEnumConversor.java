package italo.siserp.util;

import org.springframework.stereotype.Component;

import italo.siserp.model.PermissaoTipo;

@Component
public class PermissaoEnumConversor {

	public String getPermissaoTipoString( PermissaoTipo tipo ) {
		switch( tipo ) {
			case LEITURA: return "LEITURA";
			case ESCRITA: return "ESCRITA";
			case REMOCAO: return "REMOCAO";
		}
		return null;
	}
	
	public PermissaoTipo getPermissaoTipo( String tipo ) {
		if ( tipo.equalsIgnoreCase( "LEITURA" )  ) {
			return PermissaoTipo.LEITURA;
		} else if ( tipo.equalsIgnoreCase( "ESCRITA" ) ) {
			return PermissaoTipo.ESCRITA;
		} else if ( tipo.equalsIgnoreCase( "REMOCAO" ) ) {
			return PermissaoTipo.REMOCAO;
		}
		return null;
	}
		
	public String[] getPermissaoTipos() {
		PermissaoTipo[] valores = PermissaoTipo.values();
		String[] tipos = new String[ valores.length ];
		for( int i = 0; i < valores.length; i++ )
			tipos[ i ] = String.valueOf( valores[ i ] );
		return tipos;
	}
		
}

