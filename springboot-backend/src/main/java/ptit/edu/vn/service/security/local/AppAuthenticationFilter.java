package ptit.edu.vn.service.security.local;

import java.io.IOException;

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
import ptit.edu.vn.repository.TokenDiedRepository;

@Component
public class AppAuthenticationFilter extends OncePerRequestFilter {

	private UserDetailsService userDetailsService;

	private JwtService jwtService;

	private TokenDiedRepository tokenDiedRepository;

	public AppAuthenticationFilter(UserDetailsService userDetailsService, JwtService jwtService,
			TokenDiedRepository tokenDiedRepository) {
		this.userDetailsService = userDetailsService;
		this.jwtService = jwtService;
		this.tokenDiedRepository = tokenDiedRepository;
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain)
			throws ServletException, IOException {
				
			String token = jwtService.getTokenFromRequest(request); 
			String username = jwtService.getUsernameFromToken(token); 

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) { 
				UserDetails userDetails = userDetailsService.loadUserByUsername(username); 
				if (jwtService.validateToken(token, userDetails) && (tokenDiedRepository.count() > 0 && !tokenDiedRepository.existsByToken(token))) { 
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); 
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); 
					SecurityContextHolder.getContext().setAuthentication(authToken); 
				} 
			} 
			filterChain.doFilter(request, response); 
	}
}
