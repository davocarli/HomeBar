package app.davocarli.homebar.controllers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.davocarli.homebar.models.Recipe;
import app.davocarli.homebar.models.User;
import app.davocarli.homebar.services.RecipeService;
import app.davocarli.homebar.services.UserService;
import app.davocarli.homebar.util.Auth;

@RestController
@RequestMapping("/api")
public class DrinksApi {
	
	private UserService userService;
	private RecipeService recipeService;
	
	private Auth auth;
	
	public DrinksApi(
			UserService userService,
			RecipeService recipeService
			) {
		this.userService = userService;
		this.recipeService = recipeService;
		
		this.auth = new Auth(userService);
	}
	
	@GetMapping("/drinks")
	public HashMap<String, Object> getHome(@RequestParam(name="assumeduser", required=false) String username, @RequestParam(name="start", required=false) Integer startNumber, @RequestParam(name="end", required=false) Integer endNumber, HttpSession session, Model model) {
		User user = auth.authUser(session);
		HashMap<String, Object> finalResponse = new HashMap<String, Object>();
		if (username != null) {
			User assumedUser = userService.findByUsername(username);
			if (assumedUser.getShowBar()) {
				user = assumedUser;
			}
		}
		List<Recipe> recipes = recipeService.getOrdered();
		finalResponse.put("totalResults", recipes.size());
		if (startNumber != null && endNumber != null ) {
			int totalSize = recipes.size();
			if (totalSize > startNumber) {
				if (endNumber >= totalSize) {
					endNumber = recipes.size();
				}
				recipes = recipes.subList(startNumber,  endNumber);
				finalResponse.put("start", startNumber);
				finalResponse.put("end", endNumber);
			} else {
				finalResponse.put("status", "error");
				finalResponse.put("message", "start is greater than total results");
				return finalResponse;
			}
		}
		List<HashMap<String, Object>> drinks =	new LinkedList<HashMap<String, Object>>();
		for (int i = 0; i < recipes.size(); i++) {
			Recipe recipe = recipes.get(i);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", recipe.getId());
			map.put("creator", recipe.getCreator().getUsername());
			map.put("name", recipe.getName());
			map.put("ingredientsList", recipe.getIngredientList());
			map.put("averageRating", recipe.getAverageRating());
			map.put("image", recipe.getImage());
			if (user != null) {
				map.put("ingredientsFilters", recipe.getAllFullIngredients().toUpperCase().replaceAll("\'", ""));
				map.put("favorite",  recipe.isFavoritedBy(user.getId()));
			}
			drinks.add(map);
		}
		finalResponse.put("data", drinks);
		finalResponse.put("status", "success");
		return finalResponse;
	}
}
