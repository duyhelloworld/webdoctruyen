package com.duyhelloworld.service.security.local;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.servlet.http.HttpServletRequest;
import com.duyhelloworld.entity.Role;
import com.duyhelloworld.exception.AppException;
import com.duyhelloworld.service.security.usertype.LocalUser;

@Service
public class JwtService {
    @Value("${application.jwt.secret}")
    private String secretKey;

    @Value("${application.jwt.expiration}")
    private int expiration;

    @Value("${application.jwt.issuer}")
    private String issuer;

    public String generateToken(String username, String role, String email) {
        Instant iat = Instant.now();
        Instant exp = LocalDateTime.ofInstant(iat, ZoneId.systemDefault())
            .plusDays(expiration).toInstant(ZoneOffset.UTC);
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("role", role);
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(username)
                .withIssuedAt(iat)
                .withExpiresAt(exp)
                .withPayload(claims)
                .sign(getAlgorithm());
    } 

    public String generateToken(LocalUser userDetails) {
        Optional<? extends GrantedAuthority> authority = userDetails.getAuthorities().stream().findFirst();
        if (!authority.isPresent()) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống");
        }
        String role = authority.get().getAuthority();
        return generateToken(userDetails.getUsername(), role, userDetails.getEmail());
    }

    public Boolean validateToken(String token, UserDetails userDetails) { 
        final String username = getUsernameFromToken(token); 
        return username.equals(userDetails.getUsername())
            && getExpirationDateFromToken(token).after(new Date()); 
    } 

    public String getUsernameFromToken(String token) {
		return decodedJWT(token).getSubject();
	}

	public Date getIssuedAtDateFromToken(String token) {
        return decodedJWT(token).getIssuedAt();
	}
    
    public String getIssuerFromToken(String token) {
        return decodedJWT(token).getIssuer();
    }
    
	public Date getExpirationDateFromToken(String token) {
        return decodedJWT(token).getExpiresAt();
	}

    // Custom claims
    public String getEmailFromToken(String token) {
        return decodedJWT(token).getClaim("email").asString();
    }
    
    public Role getRoleFromToken(String token) {
        try {
            return Role.valueOf(decodedJWT(token).getClaim("role").asString());
        } catch (IllegalArgumentException | NullPointerException e) {
            System.out.println("Token without field 'role'");
            throw new AppException(HttpStatus.BAD_REQUEST, "Lỗi hệ thống");
        }
    }

    public Integer getUserIdFromToken(String token) {
        return decodedJWT(token).getClaim("uid").asInt();
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }
        return header.substring(7);
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secretKey.getBytes());
    }

    private DecodedJWT decodedJWT(String token) {
        if (!StringUtils.hasText(token)) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Token không hợp lệ");
        }
        return JWT.require(getAlgorithm())
            .withIssuer(issuer)
            .build()
            .verify(token);
    }
}
