package italo.siserp.security;

import java.io.IOException;
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

import io.jsonwebtoken.SignatureException;
import italo.siserp.component.JwtTokenUtil;

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
					String[] roles = jwtTokenUtil.getAuthorities( token );
										
					List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
					for( String role : roles )
						authorities.add( new SimpleGrantedAuthority( role ) );								
									
					UsernamePasswordAuthenticationToken tokenAuth = 
							new UsernamePasswordAuthenticationToken( username, null, authorities );
														
					SecurityContextHolder.getContext().setAuthentication( tokenAuth );
				} 
			} catch ( SignatureException e ) {
				
			}
		}		
		doFilter( request, response, filterChain );
	}

}
