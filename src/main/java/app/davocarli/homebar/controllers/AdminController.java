package app.davocarli.homebar.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import app.davocarli.homebar.models.Ingredient;
import app.davocarli.homebar.models.Recipe;
import app.davocarli.homebar.models.User;
import app.davocarli.homebar.services.IngredientService;
import app.davocarli.homebar.services.RecipeService;
import app.davocarli.homebar.services.UserService;
import app.davocarli.homebar.util.Auth;

@Controller
public class AdminController {
	private UserService userService;
	private RecipeService recipeService;
	private IngredientService ingredientService;
	private Auth auth;
	
	public AdminController(
			UserService userService,
			RecipeService recipeService,
			IngredientService ingredientService
			) {
		this.userService = userService;
		this.recipeService = recipeService;
		this.ingredientService = ingredientService;
		this.auth = new Auth(userService);
	}
	
	@RequestMapping("/admintasks/removebulk/removebulk/removebulk")
	public String removeBulkDrinks(HttpSession session) {
		User user = auth.authUser(session);
		if (user.getId().toString().equals("1")) {
			List<Recipe> allRecipes = recipeService.getAll();
			for (int i = allRecipes.size()-1; i >= 0; i--) {
				Recipe recipe = allRecipes.get(i);
				String source = recipe.getSource();
				if (source.equals("BULK DRINK ADDED BY HOME-BAR ADMIN TASKS")) {
					List<Ingredient> ingredients = recipe.getIngredients();
					for (int j = ingredients.size()-1; j >= 0; j--) {
						ingredientService.deleteIngredient(ingredients.get(j));
					}
					recipeService.deleteRecipe(recipe);
				}
			}
		}
		return "redirect:/";
	}
	
	@RequestMapping("/admintasks/add100/add100/add100")
	public String add100NewDrinks(HttpSession session) {
		User user = auth.authUser(session);
		if (user.getId().toString().equals("1")) {
			for (int i = 1; i <= 100; i++) {
				Recipe recipe = new Recipe();
				String idx = Integer.toString(i);
				recipe.setName("Bulk Drink " + idx);
				recipe.setInstructions("These are the instructions for Bulk Drink " + idx + ".");
				recipe.setSource("BULK DRINK ADDED BY HOME-BAR ADMIN TASKS");
				recipe.setImage("recipe" + idx + ".jpg");
				recipeService.addRecipe(recipe);
				for (int j = 1; j <= 3; j++) {
					Ingredient newIngredient = new Ingredient();
					newIngredient.setName("Bulk Drink " + idx + " Ingredient " + Integer.toString(j));
					newIngredient.setSubstituteNames("Substitute " + idx + "|" + "Substitute " + Integer.toString(j));
					newIngredient.setStatus("recipe");
					newIngredient.setAmount("some");
					newIngredient.setRecipe(recipe);
					ingredientService.addIngredient(newIngredient);
				}
			}
		}
		return "redirect:/";
	}
	
	@RequestMapping("/admintasks/fixliqueuer")
	public String fixLiqueuer(HttpSession session) {
		Object userId = session.getAttribute("user");
		if (userId != null && userId.toString().equals("1")) {
			List<Recipe> recipes = recipeService.getAll();
			
			for (int i = 0; i < recipes.size(); i++) {
				List<Ingredient> ingredients = recipes.get(i).getIngredients();
				for (int j = 0; j < ingredients.size(); j++) {
					Ingredient ingredient = ingredients.get(j);
					ingredient.setName(
							ingredient.getName()
							.replaceAll("Liqueuer", "Liqueur")
							.replaceAll("liqueuer", "liqueur")
							);
					ingredient.setSubstituteNames(
							ingredient.getSubstituteNames()
							.replaceAll("Liqueuer", "Liqueur")
							.replaceAll("liqueuer", "liqueur")
							);
					ingredientService.updateIngredient(ingredient);
				}
			}
		}
	return "redirect:/";
	}
}
