package app.davocarli.homebar.controllers;

import java.util.LinkedHashMap;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import app.davocarli.homebar.models.Ingredient;
import app.davocarli.homebar.models.User;
import app.davocarli.homebar.services.IngredientService;
import app.davocarli.homebar.services.UserService;
import app.davocarli.homebar.util.Auth;
import app.davocarli.homebar.util.IngredientUtil;
import app.davocarli.homebar.validators.IngredientValidator;

@Controller
public class BarController {
	
	private Auth auth;
	private IngredientValidator ingredientValidator;
	private IngredientService ingredientService;
	
	public BarController(
			UserService userService,
			IngredientValidator ingredientValidator,
			IngredientService ingredientService
			) {
		this.ingredientService = ingredientService;
		this.ingredientValidator = ingredientValidator;
		this.auth = new Auth(userService);
	}
	
	@RequestMapping("/bar")
	public String myBar(@ModelAttribute("ingredient") Ingredient ingredient, HttpSession session, Model model) {
		User user = auth.authUser(session, model);
		if (user != null) {
			IngredientUtil classified = new IngredientUtil(user.getIngredients());
			model.addAttribute("ingredients", classified.getStock());
			model.addAttribute("ingredientOptions", ingredientService.getIngredientNames());
			return "bar.jsp";
		}
		return "redirect:/login";
	}
	
	@PostMapping("/bar/add")
	public String addBarIngredient(@ModelAttribute("ingredient") Ingredient ingredient, BindingResult result, HttpSession session) {
		User user = auth.authUser(session);
		ingredient.setStatus("stock");
		ingredientValidator.validate(ingredient, result);
		if (user != null && !result.hasErrors()) {
			ingredient.setUser(user);
			ingredientService.addIngredient(ingredient);
		}
		return "redirect:/bar";
	}
	
	@RequestMapping("/ingredients/{id}/remove")
	public String removeIngredient(@PathVariable("id") Long id, HttpSession session, HttpServletRequest request) {
		User user = auth.authUser(session);
		Ingredient ingredient = ingredientService.getIngredient(id);
		if (user != null && user.getId().equals(ingredient.getUser().getId())) {
			ingredient.setStatus("shop");
			ingredientService.deleteIngredient(ingredient);
		}
		Optional<String> page = auth.getPreviousPageByRequest(request);
		if (!page.isPresent()) {
			return "redirect:/bar";
		}
		return page.get();
	}
	
	@RequestMapping("/shopping")
	public String myShoppingList(@ModelAttribute("ingredient") Ingredient ingredient, HttpSession session, Model model) {
		User user = auth.authUser(session);
		if (user != null) {
			IngredientUtil classified = new IngredientUtil(user.getIngredients());
			model.addAttribute("ingredients", classified.getShop());
			return "shopping.jsp";
		}
		return "redirect:/login";
	}
	
	@RequestMapping("/bar/{id}/add")
	public String moveIngredientFromShoppingToBar(@PathVariable("id") Long id, HttpSession session) {
		User user = auth.authUser(session);
		Ingredient ingredient = ingredientService.getIngredient(id);
		if (user == ingredient.getUser()) {
			ingredient.setStatus("stock");
			ingredientService.updateIngredient(ingredient);
		}
		return "redirect:/shopping";
	}
	
	@PostMapping("/shopping/add")
	public String addShoppingIngredient(@ModelAttribute("ingredient") Ingredient ingredient, BindingResult result, HttpSession session) {
		User user = auth.authUser(session);
		ingredient.setStatus("shop");
		ingredientValidator.validate(ingredient, result);
		if (user != null && !result.hasErrors()) {
			ingredient.setUser(user);
			ingredientService.addIngredient(ingredient);
		}
		return "redirect:/shopping";
	}
	
	@RequestMapping("/shopping/{id}/add")
	public String moveIngredientFromBarToShopping(@PathVariable("id") Long id, HttpSession session) {
		User user = auth.authUser(session);
		Ingredient ingredient = ingredientService.getIngredient(id);
		if (user == ingredient.getUser()) {
			ingredient.setStatus("shop");
			ingredientService.updateIngredient(ingredient);
		}
		return "redirect:/bar";
	}
	
	@RequestMapping("/ingredients/{id}/edit")
	public String editIngredient(@PathVariable("id") Long id, HttpSession session, Model model) {
		User user = auth.authUser(session, model);
		Ingredient ingredient = ingredientService.getIngredient(id);
		if (user != null && ingredient.getUser().getId().equals(user.getId())) {
			model.addAttribute("ingredient", ingredient);
			model.addAttribute("ingredientOptions", ingredientService.getIngredientNames());
			return "editIngredient.jsp";
		}
		return "redirect:/bar";
	}
	
	@PostMapping("/ingredients/{id}/edit")
	public String updateingredient(@ModelAttribute Ingredient ingredient, @PathVariable("id") Long id, HttpSession session) {
		User user = auth.authUser(session);
		Ingredient currentIngredient = ingredientService.getIngredient(id);
		if (user == null || ingredient.getName().equals("") || currentIngredient.getUser() != user) {
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
	
	//TODO: Validate that user is not null. Add this to ingredient validator.
	@PostMapping("/shopping/clone")
	public ResponseEntity<?> cloneDrinkToShopping(HttpSession session, @RequestBody LinkedHashMap<String, Long> body) {
		Long id = body.get("id");
		User user = auth.authUser(session);
		Ingredient oldIngredient = ingredientService.getIngredient(id);
		Ingredient newIngredient = new Ingredient();
		newIngredient.setName(oldIngredient.getName());
		newIngredient.setSubstituteNames(oldIngredient.getSubstituteNames());
		newIngredient.setUser(user);
		newIngredient.setStatus("shop");
		ingredientService.addIngredient(newIngredient);
		return ResponseEntity.ok("success");
	}
}
