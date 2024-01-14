package com.duyhelloworld.service.security.local;

import java.io.IOException;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import com.duyhelloworld.repository.TokenDiedRepository;

@Component
@AllArgsConstructor
public class AppAuthenticationFilter extends OncePerRequestFilter {
	private UserDetailsService userDetailsService;

	private JwtService jwtService;

	private TokenDiedRepository tokenDiedRepository;

	@Override
	protected void doFilterInternal( HttpServletRequest request,
		 HttpServletResponse response,
		 FilterChain filterChain)
			throws ServletException, IOException {
		String token = jwtService.getTokenFromRequest(request);
		if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) { 
			UserDetails userDetails = userDetailsService.loadUserByUsername(jwtService.getUsernameFromToken(token)); 
			if (jwtService.validateToken(token, userDetails) && (tokenDiedRepository.count() > 0 && !tokenDiedRepository.existsByToken(token))) { 
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); 
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); 
				SecurityContextHolder.getContext().setAuthentication(authToken); 
			} 
		} 
		filterChain.doFilter(request, response); 
	}
}
