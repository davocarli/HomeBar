package app.davocarli.homebar.services;

import java.util.List;

import org.springframework.stereotype.Service;

import app.davocarli.homebar.models.Recipe;
import app.davocarli.homebar.repositories.RecipeRepository;

@Service
public class RecipeService {
	private final RecipeRepository repo;
	
	public RecipeService(RecipeRepository repo) {
		this.repo = repo;
	}
	
	// Get All
	public List<Recipe> getAll() {
		return repo.findAll();
	}
	
	public List<Recipe> getOrdered() {
		return repo.findAllByOrderByOrdering();
	}
	
	// Get Sorted
	public List<Recipe> getSorted() {
		return repo.findAllByOrderByAverageRatingDesc();
	}
	
	// Get One (By ID)
	public Recipe getRecipe(Long id) {
		return repo.findById(id).orElse(null);
	}
	
	// Add Recipe
	public Recipe addRecipe(Recipe recipe) {
		return repo.save(recipe);
	}
	
	// Update Recipe (with ID in object)
	public Recipe updateRecipe(Recipe recipe) {
		return repo.save(recipe);
	}
	
	// Update Recipe (with ID as argument)
	public Recipe updateRecipe(Recipe recipe, Long id) {
		recipe.setId(id);
		return repo.save(recipe);
	}
	
	// Delete Recipe (by ID)
	public void deleteRecipe(Long id) {
		repo.deleteById(id);
	}
	
	// Delete Idea (by Providing Idea Object)
	public void deleteRecipe(Recipe recipe) {
		repo.delete(recipe);
	}
}
