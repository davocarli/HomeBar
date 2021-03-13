package app.davocarli.homebar.util;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;

import app.davocarli.homebar.models.User;
import app.davocarli.homebar.services.UserService;

public class Auth {
	private UserService userService;
	
	public Auth(UserService userService) {
		this.userService = userService;
	}
	
	public User authUser(HttpSession session) {
		Object userId = session.getAttribute("userId");
		if (userId != null) {
			User user = userService.findById((Long)userId);
			return user;
		}
		return null;
	}
	
	public User authUser(HttpSession session, Model model) {
		User user = authUser(session);
		model.addAttribute("user", user);
		return user;
	}
	
	protected Optional<String> getPreviousPageByRequest(HttpServletRequest request) {
	   return Optional.ofNullable(request.getHeader("Referer")).map(requestUrl -> "redirect:" + requestUrl);
	}
}
