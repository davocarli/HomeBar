package app.davocarli.homebar.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import app.davocarli.homebar.models.User;
import app.davocarli.homebar.services.IngredientService;
import app.davocarli.homebar.services.RatingService;
import app.davocarli.homebar.services.RecipeService;
import app.davocarli.homebar.services.UserService;
import app.davocarli.homebar.validators.RegistrationValidator;

@Controller
public class SiteController {
	private UserService userService;
	private RatingService ratingService;
	private RecipeService recipeService;
	private IngredientService ingredientService;
	private final RegistrationValidator registrationValidator;
	
	public SiteController(UserService userService, RatingService ratingService, RecipeService recipeService, IngredientService ingredientService, RegistrationValidator registrationValidator) {
		this.userService = userService;
		this.ratingService = ratingService;
		this.recipeService = recipeService;
		this.ingredientService = ingredientService;
		this.registrationValidator = registrationValidator;
	}
	
	@RequestMapping("/")
	public String loginPage(@ModelAttribute("user") User user, HttpSession session, Model model) {
		Object userId = session.getAttribute("User");
		if (userId != null) {
			return "redirect:/drinks";
		} else {
			return "loginRegister.jsp";
		}
	}
	
	@PostMapping("/register")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, HttpSession session, RedirectAttributes attrs) {
		registrationValidator.validate(user, result, userService);
		if (result.hasErrors()) {
			return "redirect:/";
		} else {
			user = userService.registerUser(user);
			session.setAttribute("user", user.getId());
			return "redirect:/drinks";
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
}
