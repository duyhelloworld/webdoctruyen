package ptit.edu.vn.model;

import lombok.Data;

@Data
public class SignUpModel {
    private String fullname;
    
    private String username;
    
    private String email;

    private String password;
}
