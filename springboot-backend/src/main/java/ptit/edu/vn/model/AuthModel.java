package ptit.edu.vn.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthModel {
    private String username;
    private String email;
    private String jwt;
}
