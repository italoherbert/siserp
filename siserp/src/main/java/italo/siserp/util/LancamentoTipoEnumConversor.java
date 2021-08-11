package italo.siserp.util;

import org.springframework.stereotype.Component;

import italo.siserp.model.LancamentoTipo;

@Component
public class LancamentoTipoEnumConversor {

	public String getLancamentoTipoString( LancamentoTipo tipo ) {
		switch( tipo ) {
			case CREDITO: return "CREDITO";
			case DEBITO: return "DEBITO";
		}
		return null;
	}
	
	public LancamentoTipo getLancamentoTipo( String tipo ) {
		if ( tipo.equalsIgnoreCase( "CREDITO" )  ) {
			return LancamentoTipo.CREDITO;
		} else if ( tipo.equalsIgnoreCase( "DEBITO" ) ) {
			return LancamentoTipo.DEBITO;
		}
		return null;
	}
		
	public String[] getLancamentoTipos() {
		LancamentoTipo[] valores = LancamentoTipo.values();
		String[] tipos = new String[ valores.length ];
		for( int i = 0; i < valores.length; i++ )
			tipos[ i ] = String.valueOf( valores[ i ] );
		return tipos;
	}
		
}
