package app.davocarli.homebar.controllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

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
	
	@RequestMapping("/login")
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
			return "redirect:/login";
		} else {
			session.setAttribute("user", user.getId());
			return "redirect:/";
		}
	}
	
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	
	@RequestMapping("/")
	public String drinksList(HttpSession session, Model model) {
		Object userId = session.getAttribute("user");
		List<Recipe> recipes = recipeService.getAll();
		model.addAttribute("drinks", recipes);
		if (userId != null) {
			User user = userService.findById((Long)userId);
			String stockString = user.getFullStock();
			model.addAttribute("user", user);
			List<Ingredient> unclassified = user.getIngredients();
			ArrayList<ArrayList<Ingredient>> classified = classifyIngredients(unclassified);
			ArrayList<Ingredient> stockedIngredients = classified.get(0);
			model.addAttribute("fullStock", stockString);
			model.addAttribute("stockedIngredients", stockedIngredients);
		}
		return "drinks.jsp";
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
			return "redirect:/login";
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
			return "redirect:/login";
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
			return "redirect:/login";
		}
	}
	
	@PostMapping("/drinks/new")
	public ResponseEntity<?> addDrink(HttpSession session, @RequestBody LinkedHashMap<String,Object> body) {
		Object userId = session.getAttribute("user");
		try {
			if (userId != null) {
				Recipe recipe = new Recipe();
				User user = userService.findById((Long)userId);
				recipe.setName((String)body.get("name"));
				if (recipe.getName().length() == 0) {
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
				}
				recipe.setInstructions((String)body.get("instructions"));
				recipe.setSource((String)body.get("source"));
				recipe.setCreator(user);
				recipe = recipeService.addRecipe(recipe);
				ArrayList<Object> ingredientInfo = (ArrayList<Object>)body.get("ingredients");
				for (int i = 0; i < ingredientInfo.size(); i++) {
					LinkedHashMap<String, String> ingredient = (LinkedHashMap<String, String>)ingredientInfo.get(i);
					if (ingredient.get("name").length() > 0) {
						Ingredient newIngredient = new Ingredient();
						newIngredient.setName(ingredient.get("name"));
						newIngredient.setSubstituteNames(ingredient.get("substitutes"));
						newIngredient.setAmount(ingredient.get("amount"));
						newIngredient.setRecipe(recipe);
						ingredientService.addIngredient(newIngredient);
					}
				}
				return ResponseEntity.ok(recipe.getId());
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			
		}
	}
	
	@PostMapping("/drinks/{id}/edit")
	public ResponseEntity<?> updateDrink(@PathVariable("id") Long id, HttpSession session, @RequestBody LinkedHashMap<String, Object> body) {
		Object userId = session.getAttribute("user");
		try {
			if (userId == null) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			} else {
				Recipe recipe = recipeService.getRecipe(id);
				User user = userService.findById(((Long)userId));
				if (user.getId().equals(recipe.getCreator().getId())) {
					String name = (String)body.get("name");
					if (name.length() > 0) {
						recipe.setName(name);
					}
					recipe.setSource((String)body.get("source"));
					recipe.setInstructions((String)body.get("instructions"));
					recipe = recipeService.updateRecipe(recipe);
					ArrayList<Object> ingredientInfo = (ArrayList<Object>)body.get("ingredients");
					List<Ingredient> oldIngredients = recipe.getIngredients();
					for (int i = oldIngredients.size()-1; i >= 0; i--) {
						ingredientService.deleteIngredient(oldIngredients.get(i));
					}
					for (int i = 0; i < ingredientInfo.size(); i++) {
						LinkedHashMap<String, String> ingredient = (LinkedHashMap<String, String>)ingredientInfo.get(i);
						if (ingredient.get("name").length() > 0) {
							Ingredient newIngredient = new Ingredient();
							newIngredient.setName(ingredient.get("name"));
							newIngredient.setSubstituteNames(ingredient.get("substitutes"));
							newIngredient.setAmount(ingredient.get("amount"));
							newIngredient.setRecipe(recipe);
							ingredientService.addIngredient(newIngredient);
						}
					}
					return ResponseEntity.ok(recipe.getId());
				} else {
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
				}
			}
		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	private Optional<String> getExtensionByStringHandling(String filename) {
	    return Optional.ofNullable(filename)
	      .filter(f -> f.contains("."))
	      .map(f -> f.substring(filename.lastIndexOf(".")));
	}
	
	@RequestMapping("/buckets")
	public String bucketTest() {
		AWSCredentials credentials = new BasicAWSCredentials(
				"AKIAJAEORFIVGXIWLWRA",
				"Y7mhI+8xokMJfYfYEUMdHIUHP9GGR9BbG7uVCLTR"
		);
		AmazonS3 s3client = AmazonS3ClientBuilder
				.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.US_WEST_2)
				.build();
		
		List<Bucket> buckets = s3client.listBuckets();
		for(Bucket bucket : buckets) {
			System.out.println(bucket);
			System.out.println(bucket.getName());
		}
		return "redirect:/";
	}
	
	@PostMapping("/recipe/{id}/upload")
	public ResponseEntity<?> recipeImageUpload(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file, HttpServletRequest request) {		
		AWSCredentials credentials = new BasicAWSCredentials(
				"AKIAJAEORFIVGXIWLWRA",
				"Y7mhI+8xokMJfYfYEUMdHIUHP9GGR9BbG7uVCLTR"
		);
		AmazonS3 s3client = AmazonS3ClientBuilder
				.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.US_WEST_2)
				.build();
		System.out.println("Created Client");
		try {
			String filename = file.getOriginalFilename();
			String fileExtension = getExtensionByStringHandling(filename).get();
			filename = "recipe" + id.toString() + fileExtension;
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(file.getSize());
			PutObjectRequest putRequest = new PutObjectRequest(
					"home-bar.app",
					"recipeImages/fullSize/" + filename,
					file.getInputStream(),
					metadata
			);
			s3client.putObject(putRequest);
			Recipe recipe = recipeService.getRecipe(id);
			recipe.setImage(filename);
			recipeService.updateRecipe(recipe);
		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return ResponseEntity.ok("File uploaded successfully.");
	}
	
	@RequestMapping("/drinks/{id}")
	public String drinkDetails(@PathVariable("id") Long id, HttpSession session, Model model) {
		Object userId = session.getAttribute("user");
		if (userId != null) {
			User user = userService.findById((Long)userId);
			model.addAttribute("user", user);
		}
		Recipe recipe = recipeService.getRecipe(id);
		model.addAttribute("recipe", recipe);
		return "drinkDetails.jsp";
	}
	
	@RequestMapping("/drinks/{id}/edit")
	public String editDrink(@PathVariable("id") Long id, HttpSession session, Model model) {
		Object userId = session.getAttribute("user");
		Recipe recipe = recipeService.getRecipe(id);
		if (userId != null && recipe.getCreator().getId().equals((Long)userId)) {
			model.addAttribute("recipe", recipe);
			return "editDrink.jsp";
		} else {
			return "redirect:/login";
		}
	}
	
	@RequestMapping("/ingredients/{id}/edit")
	public String editIngredient(@PathVariable("id") Long id, HttpSession session, Model model) {
		Object userId = session.getAttribute("user");
		if (userId != null) {
			User user = userService.findById((Long) userId);
			Ingredient ingredient = ingredientService.getIngredient(id);
			if (ingredient.getUser().getId() != user.getId()) {
				if (ingredient.getStatus().equals("stock")) {
					return "redirect:/bar";
				} else {
					return "redirect:/shopping";
				}
			} else {
				model.addAttribute("ingredient", ingredient);
				return "editIngredient.jsp";
			}
		} else {
			return "redirect:/login";
		}
	}
	
	@PostMapping("/ingredients/{id}/edit")
	public String updateIngredient(@ModelAttribute Ingredient ingredient, @PathVariable("id") Long id, HttpSession session, Model model) {
		Object userId = session.getAttribute("user");
		Ingredient currentIngredient = ingredientService.getIngredient(id);
		if (userId == null || ingredient.getName().equals("") || !currentIngredient.getUser().getId().equals((Long)userId)) {
			return "redirect:/ingredients/" + ingredient.getId().toString() + "/edit";
		} else {
			currentIngredient.setName(ingredient.getName());
			currentIngredient.setSubstituteNames(ingredient.getSubstituteNames());
			ingredientService.updateIngredient(currentIngredient);
			if (currentIngredient.getStatus().equals("stock")) {
				return "redirect:/bar";
			} else {
				return "redirect:/shopping";
			}
		}
	}
	
	@RequestMapping("/drinks/{id}/delete")
	public String deleteDrink(@PathVariable("id") Long id, HttpSession session) {
		Long userId = (Long)session.getAttribute("user");
		Recipe recipe = recipeService.getRecipe(id);
		if (userId.equals(recipe.getCreator().getId())) {
			List<Ingredient> ingredients = recipe.getIngredients();
			for (int i = ingredients.size()-1; i >= 0; i--) {
				ingredientService.deleteIngredient(ingredients.get(i));
			}
			recipeService.deleteRecipe(recipe);
		}
		return "redirect:/";
	}
	
	@RequestMapping("/profile")
	public String myProfile(HttpSession session) {
		Object userId = session.getAttribute("user");
		if (!userId.equals(null)) {
			return "redirect:/profile/" + userId.toString();
		}
		return "redirect:/";
	}
	
	@RequestMapping("/profile/{id}")
	public String getProfile(@PathVariable("id") Long id, HttpSession session, Model model) {
		Object userId = session.getAttribute("user");
		if (!userId.equals(null)) {
			User currentUser = userService.findById((Long)userId);
			model.addAttribute("user", currentUser);
		}
		User profileUser = userService.findById(id);
		model.addAttribute("profile", profileUser);
		return "profile.jsp";
	}
}