package app.davocarli.homebar.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import app.davocarli.homebar.models.Ingredient;
import app.davocarli.homebar.models.Recipe;
import app.davocarli.homebar.repositories.IngredientRepository;
import app.davocarli.homebar.validators.IngredientValidator;

@Service
public class IngredientService {
	private final IngredientRepository repo;
	private final String[][] keywords = { 
			{"Whiskey", "Whisky"},
			{"Rye", "Whiskey", "Rye Whiskey", "Whisky", "Rye Whisky"},
			{"Scotch", "Whiskey", "Whisky", "Scotch Whiskey", "Scotch Whisky"},
			{"Bourbon", "Bourbon Whiskey", "Bourbon Whisky", "Whiskey", "Whisky"},
			{"Single Malt", "Whiskey", "Whisky", "Single Malt Whiskey", "Single Malt Whisky"},
			{"Cognac", "Brandy"},
			{"Brandy", "Cognac"},
			{"Vodka"},
			{"Gin"},
			{"Lime", "Lime Juice"},
			{"Reposado", "Tequila"},
			{"Tequila"},
			{"Añejo", "Tequila"},
			{"Anejo", "Tequila"},
			{"Rum"},
			{"Rhum"}
			};
	
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
	
	// Get All Substitutes for all ingredients with this name
	public List<String> getSubstituteRecommendations(String ingredientName) {
		List<Ingredient> ingredients = repo.findAllByNameContainingIgnoreCaseOrSubstituteNamesContainingIgnoreCase(ingredientName, ingredientName);
		List<String> strings = new ArrayList<String>();
		
		for (int i = 0; i < ingredients.size(); i++) {
			Ingredient ingredient = ingredients.get(i);
			String subs = ingredient.getSubstituteNames();
			if (subs != null) {
				strings.addAll(Arrays.asList(subs.split("\\|")));
			}
		}
		for (int i = 0; i < keywords.length; i++) {
			if (StringUtils.containsIgnoreCase(ingredientName, keywords[i][0])) {
				strings.addAll(Arrays.asList(keywords[i][0]));
			}
		}
	
		return strings;
	}
	
	// Get All Ingredients with the selected name
	public List<Ingredient> getByName(String name) {
		return repo.findAllByName(name);
	}
	
	// Get ingredient with specific name & substitute list
	// This is used to avoid creating a brand new ingredient if
	// an identical one already exists
	public Ingredient getIngredient(String ingredientName, String substituteNames) {
		return repo.findByNameAndSubstituteNames(ingredientName, substituteNames);
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
	
	// Add list of ingredients from ArrayList<Object>
	public void addArrayOfObjects(ArrayList<Object> ingredients, Recipe recipe) {
		IngredientValidator validator = new IngredientValidator();
		for (int i = 0; i < ingredients.size(); i++) {
			LinkedHashMap<String, String> map = (LinkedHashMap<String, String>)ingredients.get(i);
			Ingredient ingredient = new Ingredient();
			ingredient.setName(map.get("name"));
			ingredient.setSubstituteNames(map.get("substitutes"));
			ingredient.setAmount(map.get("amount"));
			ingredient.setRecipe(recipe);
			ingredient.setStatus("recipe");
			if (validator.validate(ingredient)) {
				this.addIngredient(ingredient);
			}
		}
	}
}
