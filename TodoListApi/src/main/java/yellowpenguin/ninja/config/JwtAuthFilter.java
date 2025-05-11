package yellowpenguin.ninja.config;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import yellowpenguin.ninja.models.Token;
import yellowpenguin.ninja.models.User;
import yellowpenguin.ninja.repos.TokenRepository;
import yellowpenguin.ninja.repos.UserRepository;
import yellowpenguin.ninja.services.JwtService;
import yellowpenguin.ninja.services.UserService;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter{
	
	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;
	private final TokenRepository tokenRepository;
	private final UserService userService;
	


	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
			throws ServletException, IOException {
		
		if (request.getServletPath().contains("/auth")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		final String jwtToken = authHeader.substring(7);
		final String userEmail = jwtService.extractUsername(jwtToken);
		
		
		if(userEmail == null || SecurityContextHolder.getContext().getAuthentication() != null) {
			return;
		}
		
		final Token token  = tokenRepository.findByToken(jwtToken).orElse(null);
		
		if(token == null || token.isExpired() || token.isRevoked()) {
			filterChain.doFilter(request, response);
			return;
		}
		
		final UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
		User user = userService.findByEmail(userDetails.getUsername());
		
		if(user == null) {
			filterChain.doFilter(request, response);
			return;
		}
		
		final boolean isTokenValid = jwtService.isTokenValid(jwtToken, user);
		if(!isTokenValid) {
			return;
		}
		
		final var authToken  = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authToken);
		filterChain.doFilter(request, response);
		
	}
	

}
