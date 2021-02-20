package app.davocarli.homebar.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import app.davocarli.homebar.models.Rating;
import app.davocarli.homebar.models.Recipe;

@Repository
public interface RatingRepository extends CrudRepository<Rating, Long> {
	List<Rating> findAll();
	List<Rating> findAllByRecipe(Recipe recipe);
	@Query("SELECT AVG(ratingValue) FROM Rating WHERE recipe = ?1")
	Long findAverageRating(Recipe recipe);
}
