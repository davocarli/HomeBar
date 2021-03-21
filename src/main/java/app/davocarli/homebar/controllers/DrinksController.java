package app.davocarli.homebar.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import app.davocarli.homebar.models.Ingredient;
import app.davocarli.homebar.models.Recipe;
import app.davocarli.homebar.models.User;
import app.davocarli.homebar.services.IngredientService;
import app.davocarli.homebar.services.RecipeService;
import app.davocarli.homebar.services.UserService;
import app.davocarli.homebar.util.Auth;
import app.davocarli.homebar.util.IngredientUtil;
import app.davocarli.homebar.validators.RecipeValidator;

@Controller
public class DrinksController {
	
	private UserService userService;
	private RecipeService recipeService;
	private RecipeValidator recipeValidator;
	private IngredientService ingredientService;
	
	private Auth auth;
	private IngredientUtil ingredientUtil;
	
	public DrinksController(
			UserService userService,
			RecipeService recipeService,
			RecipeValidator recipeValidator,
			IngredientService ingredientService
			) {
		this.userService = userService;
		this.recipeService = recipeService;
		this.recipeValidator = recipeValidator;
		this.ingredientService = ingredientService;
		
		this.ingredientUtil = new IngredientUtil();
		this.auth = new Auth(userService);
	}
	
	@RequestMapping("/")
	public String drinksList(@RequestParam(name="assumeduser", required=false) String username, HttpSession session, Model model) {
		User user = auth.authUser(session, model);
		List<Recipe> recipes = recipeService.getAll();
		model.addAttribute("drinks", recipes);
		if (username != null) {
			user = userService.findByUsername(username);
		}
		if (user != null) {
			String stockString = user.getFullStock();
			List<Ingredient> unclassified = user.getIngredients();
			IngredientUtil classified = new IngredientUtil(unclassified);
			model.addAttribute("stockedIngredients", classified.getStock());
			model.addAttribute("fullStock", stockString);
			if (username != null) {
				model.addAttribute("assumedUser", user.getUsername());
			} else {
				model.addAttribute("assumedUser", null);
			}
		}
		return "drinks.jsp";
	}
	
	@RequestMapping("/drinks/new")
	public String newDrink(HttpSession session, Model model) {
		User user = auth.authUser(session, model);
		if (user == null) {
			return "redirect:/login";
		}
		model.addAttribute("ingredientOptions", ingredientService.getIngredientNames());
		return "newDrink.jsp";
	}
	
	@RequestMapping("/suggestions/substitutes")
	public ResponseEntity<?> getSubstitutes(@RequestParam(name="ingredient") String ingredientName, HttpSession session, Model model) {
		List<String> recs = ingredientService.getSubstituteRecommendations(ingredientName);
		
		final Map<String, Integer> counter = new HashMap<String, Integer>();
		for (int i = 0; i < recs.size(); i++) {
			String str = recs.get(i);
			counter.put(str, 1 + (counter.containsKey(str) ? counter.get(str) : 0));
		}
					
		List<String> list = new ArrayList<String>(counter.keySet());
		Collections.sort(list, new Comparator<String>() {
			@Override
			public int compare(String x, String y) {
				return counter.get(y) - counter.get(x);
			}
		});

		return ResponseEntity.ok(list);
	}
	
	@PostMapping("/drinks/new")
	public ResponseEntity<?> addDrink(HttpSession session, @RequestBody LinkedHashMap<String, Object> body, BindingResult result) {
		User user = auth.authUser(session);
		try {
			if (user != null) {
				Recipe recipe = new Recipe();
				recipe.setName((String)body.get("name"));
				recipe.setInstructions((String)body.get("instructions"));
				recipe.setSource((String)body.get("source"));
				recipe.setCreator(user);
				recipeValidator.validate(recipe, result);
				if (result.hasErrors()) {
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
				}
				recipeService.addRecipe(recipe);
				ArrayList<Object> ingredientInfo = (ArrayList<Object>)body.get("ingredients");
				ingredientService.addArrayOfObjects(ingredientInfo, recipe);
				return ResponseEntity.ok(recipe.getId());
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		} catch(Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PostMapping("/drinks/{id}/edit")
	public ResponseEntity<?> updateDrink(@PathVariable("id") Long id, HttpSession session, @RequestBody LinkedHashMap<String, Object> body, BindingResult result) {
		User user = auth.authUser(session);
		try {
			if (user != null) {
				Recipe recipe = recipeService.getRecipe(id);
				if (user.getId().equals(recipe.getCreator().getId())) {
					recipe.setName((String)body.get("name"));
					recipe.setInstructions((String)body.get("instructions"));
					recipe.setSource((String)body.get("source"));
					recipeValidator.validate(recipe, result);
					if (!result.hasErrors()) {
						recipeService.updateRecipe(recipe);
					}
					//TODO: INDICATE REASON FOR FAILURE OF VALIDATION
					ArrayList<Object> ingredients = (ArrayList<Object>)body.get("ingredients");
					List<Ingredient> oldIngredients = recipe.getIngredients();
					for (int i = oldIngredients.size() - 1; i >= 0; i--) {
						ingredientService.deleteIngredient(oldIngredients.get(i));
					}
					ingredientService.addArrayOfObjects(ingredients, recipe);
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
	
	private Optional<String> getExtensionByStringHandling(String filename) {
	    return Optional.ofNullable(filename)
	      .filter(f -> f.contains("."))
	      .map(f -> f.substring(filename.lastIndexOf(".")));
	}
	
	@PostMapping("/recipe/{id}/upload")
	public ResponseEntity<?> recipeImageUpload(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file, HttpServletRequest request) {		
		AWSCredentials credentials = new BasicAWSCredentials(
				"AKIAWLTRKZJPTGRMFB4Q",
				"shtESHl+Xp2I5gYQ1tQx2LkRVw0+0kIzBe2t2W5F"
		);
		AmazonS3 s3client = AmazonS3ClientBuilder
				.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.US_WEST_2)
				.build();
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
	public String drinkDetails(@RequestParam(name="assumeduser", required=false) String username, @PathVariable("id") Long id, HttpSession session, Model model) {
		User user = auth.authUser(session, model);
		Recipe recipe = recipeService.getRecipe(id);
		model.addAttribute("recipe", recipe);
		if (username != null) {
			user = userService.findByUsername(username);
		}
		if (user != null) {
			String stockString = user.getFullStock();
			List<Ingredient> unclassified = user.getIngredients();
			IngredientUtil classified = new IngredientUtil(unclassified);
			model.addAttribute("stockedIngredients", classified.getStock());
			model.addAttribute("fullStock", stockString);
		}
		model.addAttribute("assumedUser", username);
		return "drinkDetails.jsp";
	}
	
	@RequestMapping("/drinks/{id}/edit")
	public String editDrink(@PathVariable("id") Long id, HttpSession session, Model model) {
		User user = auth.authUser(session, model);
		Recipe recipe = recipeService.getRecipe(id);
		if (user != null && recipe.getCreator().getId().equals(user.getId())) { 
			model.addAttribute("recipe", recipe);
			model.addAttribute("ingredientOptions", ingredientService.getIngredientNames());
			return "editDrink.jsp";
		}
		return "redirect/login";
	}
	
	@RequestMapping("/drinks/{id}/delete")
	public String deleteDrink(@PathVariable("id") Long id, HttpSession session) {
		User user = auth.authUser(session);
		Recipe recipe = recipeService.getRecipe(id);
		if (user != null && user.getId().equals(recipe.getCreator().getId())) {
			List<Ingredient> ingredients = recipe.getIngredients();
			for (int i = ingredients.size()-1; i >= 0; i--) {
				ingredientService.deleteIngredient(ingredients.get(i));
			}
			recipeService.deleteRecipe(recipe);
		}
		return "redirect:/";
	}
}
