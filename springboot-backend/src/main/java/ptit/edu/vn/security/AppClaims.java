package ptit.edu.vn.security;

import java.util.Date;
import java.util.Map;

import com.auth0.jwt.interfaces.Claim;

import lombok.Data;
import ptit.edu.vn.entity.Role;

@Data
public class AppClaims {
    private String email;
    private Integer uid;
    private Role role;
    private String subject;
    private Date issueAt;
    private Date expireAt;
    private String issuer;

    public AppClaims(Map<String, Claim> claims) {
        this.subject = claims.get("sub").asString();
        this.role = Role.getRole(claims.get("role").asString());
        this.email = claims.get("email").asString();
        this.uid = Integer.parseInt(claims.get("uid").asString());
        this.issueAt = claims.get("iat").asDate();
        this.expireAt = claims.get("exp").asDate();
        this.issuer = claims.get("iss").asString();
    }
}
