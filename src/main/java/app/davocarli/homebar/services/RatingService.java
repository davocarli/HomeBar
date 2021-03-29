package app.davocarli.homebar.services;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import app.davocarli.homebar.models.Rating;
import app.davocarli.homebar.models.Recipe;
import app.davocarli.homebar.repositories.RatingRepository;
import app.davocarli.homebar.repositories.RecipeRepository;

@Service
public class RatingService {
	private final RatingRepository repo;
	private final RecipeRepository recipeRepo;
	
	public RatingService(RatingRepository repo, RecipeRepository recipeRepo) {
		this.repo = repo;
		this.recipeRepo = recipeRepo;
	}
	
	// Get All
	public List<Rating> getAll() {
		return repo.findAll();
	}
	
	// Get Ratings for a Recipe
	public List<Rating> getByRecipe(Recipe recipe) {
		return repo.findAllByRecipe(recipe);
	}
	
	// Get Average Rating for a Recipe
	public Long getAverageRating(Recipe recipe) {
		return repo.findAverageRating(recipe);
	}
	
	// Get One (By ID)
	public Rating getRating(Long id) {
		return repo.findById(id).orElse(null);
	}
	
	// Add Rating
	public Rating addRating(Rating rating) {
		return repo.save(rating);
	}
	
	// Update Rating (with ID in object)
	public Rating updateRating(Rating rating) {
		return repo.save(rating);
	}
	
	// Update Rating (with ID as argument)
	public Rating updateRating(Rating rating, Long id) {
		rating.setId(id);
		return repo.save(rating);
	}
	
	// Delete Rating (by ID)
	public void deleteRating(Long id) {
		repo.deleteById(id);
	}
	
	// Delete Rating (by providing Rating object)
	public void deleteRating(Rating rating) {
		repo.delete(rating);
	}
}
