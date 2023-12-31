package ptit.edu.vn.model;

import lombok.Data;

@Data
public class UserModel {
    private Integer id;
    private String username;
    private String email;
    private String fullname;
}
