package ptit.edu.vn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ptit.edu.vn.entity.Category;
import java.util.List;
import java.util.Optional;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("SELECT c FROM Category c WHERE c.name IN ?1")
    public List<Category> findByName(List<String> name);

    public Optional<Category> findByName(String name);

    public Boolean existsByName(String name);
}
