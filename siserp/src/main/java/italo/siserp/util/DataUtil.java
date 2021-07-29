package italo.siserp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class DataUtil {

	private final SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy" );
		
	public String dataParaString( Date date ) {
		return sdf.format( date );
	}
	
	public Date stringParaData( String data ) throws ParseException {
		return sdf.parse( data );		
	}
	
}
