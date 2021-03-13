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
import app.davocarli.homebar.services.UserService;
import app.davocarli.homebar.util.Auth;
import app.davocarli.homebar.validators.RegistrationValidator;

@Controller
public class AuthController {
	private UserService userService;
	private RegistrationValidator registrationValidator;
	private Auth auth;
	
	public AuthController(
			UserService userService,
			RegistrationValidator registrationValidator
			) {
		this.userService = userService;
		this.registrationValidator = registrationValidator;
		this.auth = new Auth(userService);
	}
		
	@RequestMapping("/login")
	public String loginPage(HttpSession session, Model model) {
		User user = auth.authUser(session, model);
		if (user == null) {
			model.addAttribute("user", new User());
			return "login.jsp";
		}
		return "redirect:/profile";
	}
	
	@RequestMapping("/register")
	public String registrationPage(HttpSession session, Model model) {
		User user = auth.authUser(session, model);
		if (user != null) {
			return "redirect:/profile";
		}
		model.addAttribute("user", new User());
		return "register.jsp";
	}
	
	@PostMapping("/register")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result) {
		registrationValidator.validate(user, result, userService);
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());
			return "redirect:/register";
		}
		user = userService.registerUser(user);
		return "redirect:/login";
	}
	
	@PostMapping("/login")
	public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password, Model model, HttpSession session, RedirectAttributes attrs) {
		User user = userService.authenticate(email, password);
		if (user == null) {
			attrs.addFlashAttribute("loginErrors", "Invalid username and password.");
			return "redirect:/login";
		}
		session.setAttribute("userId", user.getId());
		return "redirect:/";
	}
	
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
}
