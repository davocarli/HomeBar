package app.davocarli.homebar.controllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import app.davocarli.homebar.models.Ingredient;
import app.davocarli.homebar.models.Recipe;
import app.davocarli.homebar.models.User;
import app.davocarli.homebar.services.IngredientService;
import app.davocarli.homebar.services.RatingService;
import app.davocarli.homebar.services.RecipeService;
import app.davocarli.homebar.services.UserService;
import app.davocarli.homebar.validators.IngredientValidator;
import app.davocarli.homebar.validators.RegistrationValidator;

@Controller
public class SiteController {
	private UserService userService;
	private RatingService ratingService;
	private RecipeService recipeService;
	private IngredientService ingredientService;
	private final RegistrationValidator registrationValidator;
	private final IngredientValidator ingredientValidator;
	
	public SiteController(UserService userService,
			RatingService ratingService,
			RecipeService recipeService,
			IngredientService ingredientService,
			RegistrationValidator registrationValidator,
			IngredientValidator ingredientValidator) {
		this.userService = userService;
		this.ratingService = ratingService;
		this.recipeService = recipeService;
		this.ingredientService = ingredientService;
		this.registrationValidator = registrationValidator;
		this.ingredientValidator = ingredientValidator;
	}
	
	
	// Iterates through a list of ingredients and returns an ArrayList where:
	// 	Index 0 = Stocked Ingredients
	// 	Index 1 = Ingredients in Shopping List
	// 	Index 2 = Other Ingredients - In regular usage should only be recipe ingredients.
	private ArrayList<ArrayList<Ingredient>> classifyIngredients(List<Ingredient> ingredients) {
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
	
	protected Optional<String> getPreviousPageByRequest(HttpServletRequest request) {
	   return Optional.ofNullable(request.getHeader("Referer")).map(requestUrl -> "redirect:" + requestUrl);
	}
	
	@RequestMapping("/")
	public String loginPage(@ModelAttribute("user") User user, HttpSession session, Model model) {
		Object userId = session.getAttribute("user");
		if (userId != null) {
			return "redirect:/drinks";
		} else {
			return "login.jsp";
		}
	}
	
	@RequestMapping("/register")
	public String registrationPage(@ModelAttribute("user") User user, HttpSession session) {
		Object userId = session.getAttribute("user");
		if (userId != null) {
			return "redirect:/drinks";
		} else {
			return "register.jsp";
		}
	}
	
	@PostMapping("/register")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result) {
		registrationValidator.validate(user, result, userService);
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());
			return "redirect:/register";
		} else {
			user = userService.registerUser(user);
			return "redirect:/";
		}
	}
	
	@PostMapping("/login")
	public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password, Model model, HttpSession session, RedirectAttributes attrs) {
		User user = userService.authenticate(email, password);
		if (user == null) {
			attrs.addFlashAttribute("loginErrors", "Invalid username and password.");
			return "redirect:/";
		} else {
			session.setAttribute("user", user.getId());
			return "redirect:/drinks";
		}
	}
	
	@RequestMapping("/drinks")
	public String drinksList(HttpSession session, Model model) {
		Object userId = session.getAttribute("user");
		if (userId != null) {
			User user = userService.findById((Long)userId);
			String stockString = user.getFullStock();
			List<Recipe> recipes = recipeService.getAll();
			model.addAttribute("drinks", recipes);
			
			List<Ingredient> unclassified = user.getIngredients();
			ArrayList<ArrayList<Ingredient>> classified = classifyIngredients(unclassified);
			ArrayList<Ingredient> stockedIngredients = classified.get(0);
			model.addAttribute("fullStock", stockString);
			model.addAttribute("stockedIngredients", stockedIngredients);
			return "drinks.jsp";
		} else {
			return "redirect:/";
		}
	}
	
	@RequestMapping("/bar")
	public String myBar(@ModelAttribute("ingredient") Ingredient ingredient, HttpSession session, Model model) {
		Object userId = session.getAttribute("user");
		if (userId != null) {
			User user = userService.findById((Long)userId);
			List<Ingredient> unclassified = user.getIngredients();
			ArrayList<ArrayList<Ingredient>> classified = classifyIngredients(unclassified);
			ArrayList<Ingredient> stockedIngredients = classified.get(0);
			model.addAttribute("ingredients", stockedIngredients);
			return "bar.jsp";
		} else {
			return "redirect:/";
		}
	}
	
	@PostMapping("/bar/add")
	public String addBarIngredient(@ModelAttribute("ingredient") Ingredient ingredient, BindingResult result, HttpSession session) {
		Object userId = session.getAttribute("user");
		ingredient.setStatus("stock");
		ingredientValidator.validate(ingredient, result);
		if (userId != null && !result.hasErrors()) {
			User user = userService.findById((Long)userId);
			ingredient.setUser(user);
			ingredientService.addIngredient(ingredient);
		}
		return "redirect:/bar";
	}
	
	@RequestMapping("/ingredients/{id}/remove")
	public String removeBarIngredient(@PathVariable("id") Long id, HttpSession session, HttpServletRequest request) {
		Ingredient ingredient = ingredientService.getIngredient(id);
		Object userId = session.getAttribute("user");
		if (userId != null && userId.equals(ingredient.getUser().getId())) {
			ingredientService.deleteIngredient(ingredient);
		}
		return getPreviousPageByRequest(request).orElse("/");
	}
	
	@RequestMapping("/shopping/{id}/add")
	public String moveIngredientFromBarToShopping(@PathVariable("id") Long id, HttpSession session) {
		Ingredient ingredient = ingredientService.getIngredient(id);
		Object userId = session.getAttribute("user");
		if (userId != null && userId.equals(ingredient.getUser().getId())) {
			ingredient.setStatus("shop");
			ingredientService.updateIngredient(ingredient);
		}
		return "redirect:/bar";
	}
	
	@RequestMapping("/shopping")
	public String myShoppingList(@ModelAttribute("ingredient") Ingredient ingredient, HttpSession session, Model model) {
		Object userId = session.getAttribute("user");
		if (userId != null) {
			User user = userService.findById((Long)userId);
			List<Ingredient> unclassified = user.getIngredients();
			ArrayList<ArrayList<Ingredient>> classified = classifyIngredients(unclassified);
			ArrayList<Ingredient> shoppingIngredients = classified.get(1);
			model.addAttribute("ingredients", shoppingIngredients);
			return "shopping.jsp";
		} else {
			return "redirect:/";
		}
	}
	
	@RequestMapping("/bar/{id}/add")
	public String moveIngredientFromShoppingToBar(@PathVariable("id") Long id, HttpSession session) {
		Ingredient ingredient = ingredientService.getIngredient(id);
		Object userId = session.getAttribute("user");
		if (userId != null && userId.equals(ingredient.getUser().getId())) {
			ingredient.setStatus("stock");
			ingredientService.updateIngredient(ingredient);
		}
		return "redirect:/shopping";
	}
	
	@PostMapping("/shopping/add")
	public String addShoppingIngredient(@ModelAttribute("ingredient") Ingredient ingredient, BindingResult result, HttpSession session) {
		Object userId = session.getAttribute("user");
		ingredient.setStatus("shop");
		ingredientValidator.validate(ingredient, result);
		if (userId != null && !result.hasErrors()) {
			User user = userService.findById((Long)userId);
			ingredient.setUser(user);
			ingredientService.addIngredient(ingredient);
		}
		return "redirect:/shopping";
	}
	
	@RequestMapping("/drinks/new")
	public String newDrink(HttpSession session, Model model) {
		Object userId = session.getAttribute("user");
		if (userId != null) {
			return "newDrink.jsp";
		} else {
			return "redirect:/";
		}
	}
	
	@PostMapping("/drinks/new")
	public String addDrink(HttpSession session, @RequestBody LinkedHashMap<String,Object> body) {
		Object userId = session.getAttribute("user");
		if (userId != null) {
			Recipe recipe = new Recipe();
			User user = userService.findById((Long)userId);
			recipe.setName((String)body.get("name"));
			recipe.setInstructions((String)body.get("instructions"));
			recipe.setSource((String)body.get("source"));
			recipe.setCreator(user);
			recipe = recipeService.addRecipe(recipe);
			List<Ingredient> ingredients = new ArrayList<Ingredient>();
			ArrayList<Object> ingredientInfo = (ArrayList<Object>)body.get("ingredients");
			for (int i = 0; i < ingredientInfo.size(); i++) {
				Ingredient newIngredient = new Ingredient();
				LinkedHashMap<String, String> ingredient = (LinkedHashMap<String, String>)ingredientInfo.get(i);
				newIngredient.setName(ingredient.get("name"));
				newIngredient.setSubstituteNames(ingredient.get("substitutes"));
				newIngredient.setAmount(ingredient.get("amount"));
				newIngredient.setRecipe(recipe);
				ingredientService.addIngredient(newIngredient);
			}
		} else {
			return "redirect:/";
		}
		return "redirect:/";
	}
}
