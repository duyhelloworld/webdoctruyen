package ptit.edu.vn.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private TokenDiedRepository tokenDiedRepository;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain)
			throws ServletException, IOException {
			String authHeader = request.getHeader("Authorization"); 
			String token = null; 
			String username = null; 
			if (authHeader != null && authHeader.startsWith("Bearer ")) { 
				token = authHeader.substring(7); 
				username = jwtService.getUsernameFromToken(token); 
			} 
		
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
