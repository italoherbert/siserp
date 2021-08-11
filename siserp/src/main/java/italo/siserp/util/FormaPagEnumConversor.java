package italo.siserp.util;

import org.springframework.stereotype.Component;

import italo.siserp.model.FormaPag;

@Component
public class FormaPagEnumConversor {

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
		
	public String[] getFormasPags() {
		FormaPag[] valores = FormaPag.values();
		String[] formas = new String[ valores.length ];
		for( int i = 0; i < valores.length; i++ )
			formas[ i ] = String.valueOf( valores[ i ] );
		return formas;
	}
			
}
