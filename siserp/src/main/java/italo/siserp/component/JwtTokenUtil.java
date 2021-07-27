package italo.siserp.component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {
	
	private final long JWT_TOKEN_VALIDADE = 5 * 60 * 60 * 1000;
	
	@Value( "jwt.secret" )
	private String secret;
	
	public String extraiBearerToken( String authorizationHeader ) {
		return authorizationHeader.replace( "Bearer ", "" );
	}
	
	public String geraToken( String username, String[] roles ) {		
		Map<String, Object> claims = new HashMap<String, Object>();
		String strRoles = "";
		for( String role : roles ) {
			if ( !strRoles.isEmpty() )
				strRoles += ",";
			strRoles += role;
		}
				
		claims.put( "roles", strRoles );
		
		return geraToken( claims, username );				
	}
	
	public String geraToken( Map<String, Object> claims, String subject ) {
		return Jwts.builder().setClaims( claims ).setSubject( subject ).setIssuedAt( new Date() )
				.setExpiration( new Date( System.currentTimeMillis() + JWT_TOKEN_VALIDADE ) )
				.signWith( SignatureAlgorithm.HS512, secret ).compact();
	}
	
	public boolean validaToken( String token ) {
		if ( this.isTokenExpirado( token ) )
			return false;
		return this.getSubject( token ) != null;  
	}
	
	public boolean isUsuarioAdmin( String token ) {
		String[] roles = this.getAuthorities( token );		
		for( String role : roles )
			if ( role.equalsIgnoreCase( "ADMIN" ) )
				return true;
		
		return false;
	}
	
	public boolean isTokenExpirado( String token ) {
		final Date expiration = this.getExpirationDate( token );
		return expiration.before( new Date() );
	}
	
	public Date getExpirationDate( String token ) {
		return this.getTokenClaims( token ).getExpiration();
	}
		
	public String getSubject( String token ) {
		return this.getTokenClaims( token ).getSubject();
	}
				
	public String[] getAuthorities( String token ) {		
		Object roles = this.getTokenClaims( token ).get( "roles" );
		if ( roles != null )
			return String.valueOf( roles ).split( "," );
		
		return new String[] {};
	}
	
	public Claims getTokenClaims( String token ) {
		return Jwts.parser().setSigningKey( secret ).parseClaimsJws( token ).getBody();
	}	

}
