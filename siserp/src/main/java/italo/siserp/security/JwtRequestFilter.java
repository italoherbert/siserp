package italo.siserp.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import italo.siserp.util.JwtTokenUtil;

public class JwtRequestFilter extends OncePerRequestFilter {
	
	private JwtTokenUtil jwtTokenUtil;
	
	public JwtRequestFilter( JwtTokenUtil jwtTokenUtil ) {
		this.jwtTokenUtil = jwtTokenUtil;
	}
	
	@Override
	protected void doFilterInternal( HttpServletRequest request, HttpServletResponse response, FilterChain filterChain ) throws ServletException, IOException {		
		String auth = request.getHeader( "Authorization" );
		if( auth != null ) {					
			String token = jwtTokenUtil.extraiBearerToken( auth );
			try {
				if ( !jwtTokenUtil.isTokenExpirado(token) ) {
					String username = jwtTokenUtil.getSubject( token );
					String[] authorities = jwtTokenUtil.getAuthorities( token );
																
					List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
					for( String grantedAuthority : authorities )						
						grantedAuthorities.add( new SimpleGrantedAuthority( grantedAuthority ) );					
									
					UsernamePasswordAuthenticationToken tokenAuth = 
							new UsernamePasswordAuthenticationToken( username, null, grantedAuthorities );
														
					SecurityContextHolder.getContext().setAuthentication( tokenAuth );
				} 
			} catch ( SignatureException e ) {
				
			} catch ( ExpiredJwtException e ) {
				String resp = "{ \"mensagem\" : \"Token expirado, por favor faça login novamente.\" }";
				response.setContentType( "application/json" );
				response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
				
				PrintWriter writer = new PrintWriter( response.getOutputStream() );
				writer.print( resp ); 
				writer.flush();		
				
				return;
			}
		}		
		doFilter( request, response, filterChain );
	}

}
