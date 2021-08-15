package italo.siserp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class DataUtil {

	private final SimpleDateFormat dateFormat = new SimpleDateFormat( "dd/MM/yyyy" );
	private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat( "dd/MM/yyyy hh:mm:ss" );
		
	public String dataParaString( Date date ) {
		return dateFormat.format( date );
	}
	
	public Date stringParaData( String data ) throws ParseException {
		return dateFormat.parse( data );		
	}
	
	public String dataTimeParaString( Date data ) {
		return dateTimeFormat.format( data );
	}
	
	public Date apenasData( Date data ) {
		String dstr = dateFormat.format( data );
		try {
			return dateFormat.parse( dstr );
		} catch (ParseException e) {
			return data;
		}		
	}
	
	public Date addUmDia( Date data ) {
		Calendar c = Calendar.getInstance();
		c.setTime( data );
		
		c.add( Calendar.DATE, 1 );
		
		return c.getTime();
	}
	
}
