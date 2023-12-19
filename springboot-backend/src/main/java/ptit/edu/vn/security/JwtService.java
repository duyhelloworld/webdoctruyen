package ptit.edu.vn.security;

import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

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

    public String generateToken(Integer userId, String username, String role, String email) {
        Date iat = new Date();
        Date exp = new Date(iat.getTime() + expiration * 1000);
        if (!Role.isValidRole(role)) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống");
        }
        return JWT.create()
                .withIssuer(issuer)
                .withIssuedAt(iat)
                .withExpiresAt(exp)
                .withSubject(username)
                .withClaim("role", role)
                .withClaim("uid", userId)
                .withClaim("email", email)
                .sign(Algorithm.HMAC256(secretKey));
    } 

    public String generateToken(AppUserDetails userDetails) {
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        if (Role.isValidRole(role)) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống");
        }
        return generateToken(userDetails.getId(), userDetails.getUsername(), role, userDetails.getEmail());
    }

    public boolean validTokenType(String token) {
        try {
            JWT
                .require(Algorithm.HMAC256(secretKey))
                .withIssuer(issuer)
                .build()
                .verify(token);
            // Verified: iss, exp, alg
            return true;
        } catch (JWTVerificationException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean validateToken(String token, UserDetails userDetail) {
        return getUsernameFromToken(token).equals(userDetail.getUsername()) 
            && userDetail.isEnabled()
            && userDetail.isCredentialsNonExpired()
            && userDetail.isAccountNonExpired()
            && userDetail.isAccountNonLocked();
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }
        return header.substring(7);
    }

    private <T> T getClaimFromToken(String token, Function<AppClaims, T> claimsResolver) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)).withIssuer(issuer).build();
        final AppClaims claims = new AppClaims(verifier.verify(token).getClaims());
        return claimsResolver.apply(claims);
    }
    
    public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, AppClaims::getSubject);
	}

	public Date getIssuedAtDateFromToken(String token) {
		return getClaimFromToken(token, AppClaims::getIssueAt);
	}

    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, AppClaims::getEmail);
    }

    public String getIssuerFromToken(String token) {
        return getClaimFromToken(token, AppClaims::getIssuer);
    }

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, AppClaims::getExpireAt);
	}

    public Role getRoleFromToken(String token) {
        return getClaimFromToken(token, AppClaims::getRole);
    }

    public Integer getUserIdFromToken(String token) {
        return getClaimFromToken(token, AppClaims::getUid);
    }
}
