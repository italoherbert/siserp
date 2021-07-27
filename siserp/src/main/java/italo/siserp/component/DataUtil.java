package italo.siserp.component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class DataUtil {

	private final SimpleDateFormat sdfParaData = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" );
	private final SimpleDateFormat sdfParaString = new SimpleDateFormat( "dd/MM/yyyy hh:mm:ss" );
		
	public String dataParaString( Date date ) {
		return sdfParaString.format( date );
	}
	
	public Date stringParaData( String data ) throws ParseException {
		return sdfParaData.parse( data );		
	}
	
}
