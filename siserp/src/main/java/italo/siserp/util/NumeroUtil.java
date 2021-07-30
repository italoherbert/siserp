package italo.siserp.util;

import java.text.DecimalFormat;
import java.text.ParseException;

import org.springframework.stereotype.Component;

import italo.siserp.exception.InteiroInvalidoException;

@Component
public class NumeroUtil {
	
	private DecimalFormat df = new DecimalFormat( "0.00" );
	
	public String doubleParaString( double numero ) {
		return df.format( numero );
	}
	
	public double stringParaDouble( String numero ) throws ParseException {
		return df.parse( numero ).doubleValue();		
	}
	
	public String inteiroParaString( int numero ) {
		return String.valueOf( numero );
	}
	
	public int stringParaInteiro( String numero ) throws InteiroInvalidoException {
		try {
			return Integer.parseInt( numero );
		} catch ( NumberFormatException e ) {
			throw new InteiroInvalidoException();
		}
	}
	
}
