package ptit.edu.vn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ptit.edu.vn.entity.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer>{
    
}
