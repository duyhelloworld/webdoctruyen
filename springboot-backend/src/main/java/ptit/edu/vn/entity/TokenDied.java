package ptit.edu.vn.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
class TokenDiedId {
    User user;
    LocalDateTime diedAt;
}

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(TokenDiedId.class)
public class TokenDied {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    private LocalDateTime diedAt;

    @Column(columnDefinition = "text")
    private String token;
}
