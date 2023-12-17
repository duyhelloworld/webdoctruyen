package ptit.edu.vn.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fullName = "Không biết";
    
    @Column(nullable = false, unique = true)
    private String username;

    // Nếu user ko đặt avatar thì sẽ lấy avatar mặc định
    @Column(nullable = false)
    private String avatar = "default-avatar.png";

    @Column(nullable = false, unique = true)
    private String email;

    // pass sẽ được hash trước khi vào đây
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public User(String username, String email, String password, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String username, String email, String password, String avatar, String fullName, Role role) {
        this(username, email, password, role);
        this.avatar = avatar;
        this.fullName = fullName;
    }

    @OneToMany(mappedBy = "user")
    private List<Comment> Comments;

    @OneToMany(mappedBy = "user")
    private List<Rating> Ratings;

    @OneToMany(mappedBy = "user")
    private List<TokenDied> TokenDieds;
}
