package app.davocarli.homebar.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import app.davocarli.homebar.models.Ingredient;
import app.davocarli.homebar.models.Recipe;
import app.davocarli.homebar.services.IngredientService;
import app.davocarli.homebar.services.RecipeService;
import app.davocarli.homebar.services.UserService;

@Controller
public class AdminController {
	private UserService userService;
	private RecipeService recipeService;
	private IngredientService ingredientService;
	
	public AdminController(
			UserService userService,
			RecipeService recipeService,
			IngredientService ingredientService
			) {
		this.userService = userService;
		this.recipeService = recipeService;
		this.ingredientService = ingredientService;
	}
	
	@RequestMapping("/admintasks/fixliqueuer")
	public String fixLiqueuer(HttpSession session) {
		Object userId = session.getAttribute("user");
		if (userId != null && userId.equals(1)) {
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
