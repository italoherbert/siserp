package italo.siserp.util;

import org.springframework.stereotype.Component;

import italo.siserp.exception.DoubleInvalidoException;
import italo.siserp.exception.InteiroInvalidoException;

@Component
public class NumeroUtil {
		
	public String doubleParaString( double numero ) {
		return String.valueOf( numero );
	}
	
	public double stringParaDouble( String numero ) throws DoubleInvalidoException {
		try {
			return Double.parseDouble( numero );
		} catch ( NumberFormatException e ) {
			throw new DoubleInvalidoException();
		}		
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
