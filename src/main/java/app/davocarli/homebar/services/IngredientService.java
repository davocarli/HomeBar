package app.davocarli.homebar.services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import app.davocarli.homebar.models.Ingredient;
import app.davocarli.homebar.repositories.IngredientRepository;

@Service
public class IngredientService {
	private final IngredientRepository repo;
	
	public IngredientService(IngredientRepository repo) {
		this.repo = repo;
	}
	
	// Get All
	public List<Ingredient> getAll() {
		return repo.findAll();
	}
	
	// Get All Ingredient Names
	public List<String> getIngredientNames() {
		return repo.findAllIngredientNames();
	}
	
	// Get All Substitutes for this Ingredient Name
	public String getSubstituteRecommendations(String ingredientName) {
		List<String> allSubstitutes = repo.findIngredientRecommendations(ingredientName);
		String joinedList = String.join("\n", allSubstitutes);
		Set<String> uniqueSet = new HashSet<String>(Arrays.asList(joinedList.split("\n")));
		String finalList = String.join("\n", uniqueSet);
		return finalList;
	}
	
	// Get All Ingredients with the selected name
	public List<Ingredient> getByName(String name) {
		return repo.findAllByName(name);
	}
	
	// Get ingredient with specific name & substitute list
	// This is used to avoid creating a brand new ingredient if
	// an identical one already exists
	public Ingredient getIngredient(String ingredientName, String substituteNames) {
		return repo.findByIngredientNameAndBySubstituteNames(ingredientName, substituteNames);
	}
	
	// Get One (By ID)
	public Ingredient getIngredient(Long id) {
		return repo.findById(id).orElse(null);
	}
	
	// Add Ingredient
	public Ingredient addIngredient(Ingredient ingredient) {
		return repo.save(ingredient);
	}
	
	// Update Ingredient (with ID in object)
	public Ingredient updateIngredient(Ingredient ingredient) {
		return repo.save(ingredient);
	}
	
	// Update Ingredient (with ID as argument)
	public Ingredient updateIngredient(Ingredient ingredient, Long id) {
		ingredient.setId(id);
		return repo.save(ingredient);
	}
	
	// Delete Ingredient (by ID)
	public void deleteIngredient(Long id) {
		repo.deleteById(id);
	}
	
	// Delete Idea (by Providing Idea Object)
	public void deleteIngredient(Ingredient ingredient) {
		repo.delete(ingredient);
	}
}
