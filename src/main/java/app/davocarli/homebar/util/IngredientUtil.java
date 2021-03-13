package app.davocarli.homebar.util;

import java.util.ArrayList;
import java.util.List;

import app.davocarli.homebar.models.Ingredient;

public class IngredientUtil {
	
	private List<Ingredient> stock;
	private List<Ingredient> shop;
	private List<Ingredient> recipe;
	
	public IngredientUtil() {}
	
	public IngredientUtil(List<Ingredient> ingredients) {
		ArrayList<ArrayList<Ingredient>> classified = this.classifyIngredients(ingredients);
		this.stock = classified.get(0);
		this.shop = classified.get(1);
		this.recipe = classified.get(2);
	}
	
	// Iterates through a list of ingredients and returns an ArrayList where:
	// 	Index 0 = Stocked Ingredients
	// 	Index 1 = Ingredients in Shopping List
	// 	Index 2 = Other Ingredients - In regular usage should only be recipe ingredients.
	public ArrayList<ArrayList<Ingredient>> classifyIngredients(List<Ingredient> ingredients) {
		ArrayList<ArrayList<Ingredient>> result = new ArrayList<ArrayList<Ingredient>>(3);
		for (int i = 0; i < 3; i++) {
			result.add(new ArrayList<Ingredient>());
		}
		for (int i = 0; i < ingredients.size(); i++) {
			Ingredient ingredient = ingredients.get(i);
			if (ingredient.getStatus().equals("stock")) {
				result.get(0).add(ingredient);
			} else if (ingredient.getStatus().equals("shop")) {
				result.get(1).add(ingredient);
			} else {
				result.get(2).add(ingredient);
			}
		}
		return result;
	}
	
	public List<Ingredient> getStock() {
		return stock;
	}
	public List<Ingredient> getStocked() {
		return this.getStock();
	}
	
	public List<Ingredient> getShop() {
		return this.shop;
	}
	public List<Ingredient> getShopping() {
		return this.getShop();
	}
	public List<Ingredient> getShoppingList() {
		return this.getShop();
	}
	
	public List<Ingredient> getRecipe() {
		return this.recipe;
	}
	public List<Ingredient> getRecipeIngredients() {
		return this.getRecipe();
	}
}
