package ptit.edu.vn.service.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import ptit.edu.vn.entity.Role;
import ptit.edu.vn.exception.AppException;

@Service
public class JwtService {
    @Value("${application.jwt.secret}")
    private String secretKey;

    @Value("${application.jwt.expiration}")
    private int expiration;

    @Value("${application.jwt.issuer}")
    private String issuer;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    private JwtParser getParser() {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .requireIssuer(issuer)
            .build();
    }

    public String generateToken(Integer userId, String username, String role, String email) {
        Date iat = new Date();
        Date exp = new Date(iat.getTime() + expiration * 24 * 60 * 60 * 1000);
        if (!Role.isValidRole(role)) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống");
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("uid", userId);
        claims.put("email", email);
        return Jwts.builder()
                .issuer(issuer)
                .issuedAt(iat)
                .subject(username)
                .expiration(exp)
                .claims(claims)
                .signWith(getSigningKey()).compact();
    } 

    public String generateToken(AppUserDetails userDetails) {
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        if (!Role.isValidRole(role)) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống");
        }
        return generateToken(userDetails.getId(), userDetails.getUsername(), role, userDetails.getEmail());
    }

    public Boolean validateToken(String token, UserDetails userDetails) { 
        final String username = getUsernameFromToken(token); 
        return username.equals(userDetails.getUsername())
            && getExpirationDateFromToken(token).after(new Date()); 
    } 

    private <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getParser().parseSignedClaims(token).getPayload();
        return claimsResolver.apply(claims);
    }
    
    public String getUsernameFromToken(String token) {
		return getClaimsFromToken(token, Claims::getSubject);
	}

	public Date getIssuedAtDateFromToken(String token) {
		return getClaimsFromToken(token, Claims::getIssuedAt);
	}
    
    public String getIssuerFromToken(String token) {
        return getClaimsFromToken(token, Claims::getIssuer);
    }
    
	public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token, Claims::getExpiration);
	}

    // Custom claims
    public String getEmailFromToken(String token) {
        return getParser().parseSignedClaims(token).getPayload().get("email", String.class);
    }

    public Role getRoleFromToken(String token) {
        return Role.getRole(getParser().parseSignedClaims(token).getPayload().get("role", String.class));
    }

    public Integer getUserIdFromToken(String token) {
        return getParser().parseSignedClaims(token).getPayload().get("uid", Integer.class);
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }
        return header.substring(7);
    }
}
