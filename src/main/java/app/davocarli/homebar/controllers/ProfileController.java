package app.davocarli.homebar.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import app.davocarli.homebar.models.User;
import app.davocarli.homebar.services.UserService;
import app.davocarli.homebar.util.Auth;
import app.davocarli.homebar.validators.ProfileValidator;

@Controller
public class ProfileController {
	
	private Auth auth;
	private UserService userService;
	private ProfileValidator profileValidator;
	
	public ProfileController(
			UserService userService,
			ProfileValidator profileValidator
			) {
		this.userService = userService;
		this.profileValidator = profileValidator;
		
		this.auth = new Auth(userService);
	}
	
	@RequestMapping("/profile")
	public String myProfile(HttpSession session) {
		User user = auth.authUser(session);
		if (user != null) {
			return "redirect:/profile/" + user.getUsername();
		}
		return "redirect/login";
	}
	
	@RequestMapping("/profile/{username}")
	public String getProfile(@PathVariable("username") String username, HttpSession session, Model model) {
		User profileUser = userService.findByUsername(username);
		if (profileUser == null) {
			return "redirect:/";
		}
		model.addAttribute("profile", profileUser);
		auth.authUser(session, model);
		return "profile.jsp";
	}
	
	@RequestMapping("/profile/edit")
	public String editProfile(HttpSession session, Model model) {
		User user = auth.authUser(session, model);
		if (user == null) {
			return "redirect:/login";
		}
		return "editProfile.jsp";
	}
	
	@PostMapping("/profile/edit")
	public String updateProfile(@Valid @ModelAttribute User user, HttpSession session, BindingResult result) {
		User currentUser = auth.authUser(session);
		if (user != null) {
			currentUser.setBio(user.getBio());
			currentUser.setFirstName(user.getFirstName());
			currentUser.setLastName(user.getLastName());
			currentUser.setShowName(user.getShowName());
			currentUser.setShowBar(user.getShowBar());
			
			profileValidator.validate(currentUser, result);
			if (!result.hasErrors()) {
				userService.updateUser(currentUser);
			}
		}
		return "redirect:/profile";
	}
}
