package italo.siserp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import italo.siserp.util.JwtTokenUtil;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
		
	private final String[] PUBLIC = {
		"/**", "/js/**", "/css/**", 
		"/api/login/entrar", "/h2-console/**"
	};
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
			.sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS ).and()
			.addFilterBefore( new JwtRequestFilter( jwtTokenUtil ), UsernamePasswordAuthenticationFilter.class )
			.authorizeRequests()
				.antMatchers( PUBLIC ).permitAll() 								
				.anyRequest().authenticated();
		
		http.headers().frameOptions().disable();
	}
	
}
