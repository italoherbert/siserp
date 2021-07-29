package italo.siserp.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

@Component
public class HashUtil {
	
	public String geraHash( String pw ) {
		try {
			MessageDigest md = MessageDigest.getInstance( "SHA-256" );
			byte[] cript = md.digest( pw.getBytes() );			
			return Hex.encodeHexString( cript );
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
	
	public static void main(String[] args) {
		System.out.println( new HashUtil().geraHash( "admin" ) );
	}
		
}
