package ptit.edu.vn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ptit.edu.vn.entity.TokenDied;

public interface TokenDiedRepository extends JpaRepository<TokenDied, Long>{
    public boolean existsByToken(String token);
}
