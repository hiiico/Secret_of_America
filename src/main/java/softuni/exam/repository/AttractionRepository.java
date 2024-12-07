package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Attraction;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttractionRepository extends JpaRepository<Attraction,Long> {
    Optional<Attraction> findByName(String name);
    List<Attraction> findByTypeOrTypeAndElevationGreaterThanEqualOrderByNameAscCountry(String type1, String type2, int elevation);
}
