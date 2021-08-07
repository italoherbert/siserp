package italo.siserp.util;

import org.springframework.stereotype.Component;

import italo.siserp.model.FormaPag;
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
	
	public String getFormaPagString( FormaPag formaPag ) {
		switch( formaPag ) {
			case CARTAO: return "CARTAO";
			case ESPECIE: return "ESPECIE";
			case DEBITO: return "DEBITO";
		}
		return null;
	}
	
	public FormaPag getFormaPag( String formaPag ) {
		if ( formaPag.equalsIgnoreCase( "CARTAO" ) ) {
			return FormaPag.CARTAO;			
		} else if ( formaPag.equalsIgnoreCase( "ESPECIE") ) {
			return FormaPag.ESPECIE;
		} else if ( formaPag.equalsIgnoreCase( "DEBITO" ) ) {
			return FormaPag.DEBITO;
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
	
	public String[] getFormasPags() {
		FormaPag[] valores = FormaPag.values();
		String[] modos = new String[ valores.length ];
		for( int i = 0; i < valores.length; i++ )
			modos[ i ] = String.valueOf( valores[ i ] );
		return modos;
	}
	
}
