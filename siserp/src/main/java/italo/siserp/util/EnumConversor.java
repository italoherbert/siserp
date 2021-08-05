package italo.siserp.util;

import org.springframework.stereotype.Component;

import italo.siserp.model.ModoPag;
import italo.siserp.model.UsuarioTipo;

@Component
public class EnumConversor {

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
	
	public String getModoPagString( ModoPag modoPag ) {
		switch( modoPag ) {
			case CARTAO: return "CARTAO";
			case ESPECIE: return "ESPECIE";
			case DEBITO: return "DEBITO";
		}
		return null;
	}
	
	public ModoPag getModoPag( String modoPag ) {
		if ( modoPag.equalsIgnoreCase( "CARTAO" ) ) {
			return ModoPag.CARTAO;			
		} else if ( modoPag.equalsIgnoreCase( "ESPECIE") ) {
			return ModoPag.ESPECIE;
		} else if ( modoPag.equalsIgnoreCase( "DEBITO" ) ) {
			return ModoPag.DEBITO;
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
	
	public String[] getModosPags() {
		ModoPag[] valores = ModoPag.values();
		String[] modos = new String[ valores.length ];
		for( int i = 0; i < valores.length; i++ )
			modos[ i ] = String.valueOf( valores[ i ] );
		return modos;
	}
	
}
