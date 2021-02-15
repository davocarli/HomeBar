package app.davocarli.homebar.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import app.davocarli.homebar.models.Ingredient;

@Repository
public interface IngredientRepository extends CrudRepository<Ingredient, Long> {
	List<Ingredient> findAll();
	List<Ingredient> findAllByName(String name);
	@Query("SELECT DISTINCT substitutes FROM Ingredients WHERE ingredientName = ?1")
	List<String> findIngredientRecommendations(String ingredientName);
	@Query("SELECT DISTINCT ingredientName FROM Ingredients")
	List<String> findAllIngredientNames();
	Ingredient findByIngredientNameAndBySubstituteNames(String ingredientName, String substituteNames);
}
