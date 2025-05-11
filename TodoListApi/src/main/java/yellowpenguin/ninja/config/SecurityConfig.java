package yellowpenguin.ninja.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import yellowpenguin.ninja.models.Token;
import yellowpenguin.ninja.services.AuthService;

@Configuration
public class SecurityConfig {
	
	@Autowired
	private JwtAuthFilter jwtAuthFilter;
	
	//puede que pete por este autowired OJO.
	@Autowired
	private AuthService authService;
	

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .logout(logout-> logout.logoutUrl("/auth/logout").addLogoutHandler((request, response, authentication) -> {
            	final var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            	authService.logout(authHeader);
            }).logoutSuccessHandler((request,response,authentication) -> SecurityContextHolder.clearContext()));
            ;

        return http.build();
    }
	
}


	
