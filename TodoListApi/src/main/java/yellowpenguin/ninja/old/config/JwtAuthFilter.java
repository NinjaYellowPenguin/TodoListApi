package yellowpenguin.ninja.config;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;
	private final TaskService userService;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {

		if(filterAuthorizedPaths(request, response)) {
			filterChain.doFilter(request, response);
			return;
		}

		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if(!jwtService.validateToken(authHeader)) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Token is invalid or expired");
			return;
		}		

		final UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
		User user = userService.findByEmail(userDetails.getUsername());

		if (user == null) {
			// filterChain.doFilter(request, response);
			return;
		}

		final boolean isTokenValid = jwtService.isTokenValid(jwtToken, user);
		if (!isTokenValid) {
			return;
		}

		final var authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authToken);
		filterChain.doFilter(request, response);

	}

	private boolean filterAuthorizedPaths(HttpServletRequest request, HttpServletResponse response) {
		if(request.getServletPath().contains("/auth")) {
			return true;
		}
		return false;
	}

}
