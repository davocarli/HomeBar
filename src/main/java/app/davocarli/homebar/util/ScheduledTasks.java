package app.davocarli.homebar.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import app.davocarli.homebar.models.Recipe;
import app.davocarli.homebar.services.RecipeService;

@Component
public class ScheduledTasks {
	
	@Autowired
	private RecipeService recipeService;
		
	@Scheduled(cron = "0 0 * * * ?")
	public void randomizeRecipes() {
		List<Recipe> recipes = recipeService.getAll();
		for (int i = 0; i < recipes.size(); i++) {
			Recipe recipe = recipes.get(i);
			recipe.setOrder((Double)Math.random());
			recipeService.updateRecipe(recipe);
		}
	}
}
