package italo.siserp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
	
	public Date apenasData( Date data ) {
		Calendar c = Calendar.getInstance();
		c.setTime( data );
		
		c.set( Calendar.HOUR, 0 );
		c.set( Calendar.MINUTE, 0 );
		c.set( Calendar.SECOND, 0 );
		c.set( Calendar.MILLISECOND, 0); 
		
		return c.getTime();
	}
	
}
