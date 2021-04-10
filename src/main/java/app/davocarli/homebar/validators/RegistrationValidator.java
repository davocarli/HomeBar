package app.davocarli.homebar.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import app.davocarli.homebar.models.User;
import app.davocarli.homebar.services.UserService;

@Component
public class RegistrationValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}
	
	public void validate(Object target, Errors errors, UserService service) {
		User user = (User)target;
		
		// If password is exactly "--ADMINUSER--" skip validation
		if (user.getPassword().equals("--ADMINUSER--")) {
			return;
		}
		
		// Passwords don't match
		if (!user.getPassword().equals(user.getPasswordConfirmation())) {
			errors.rejectValue("passwordConfirmation", "MATCH", "Your passwords do not match.");
		} 
		
		// Password is too short
		if (user.getPassword().length() < 8) {
			errors.rejectValue("password", "LENGTH", "Your password must be at least 8 characters.");
		} 
		
		// First Name is blank
		if (user.getFirstName().length() < 1) {
			errors.rejectValue("firstName", "EMPTY", "Please enter a first name.");
		}
		
		// Last Name is blank
		if (user.getLastName().length() < 1) {
			errors.rejectValue("lastName", "EMPTY", "Please enter a last name.");
		}
		
		// Username is too short
		if (user.getUsername().length() < 4) {
			errors.rejectValue("username", "LENGTH", "Your username must be at least 4 characters.");
		}
		
		// Username is taken
		if (service != null && service.findByUsername(user.getUsername()) != null) {
			errors.rejectValue("username", "TAKEN", "This username has been taken.");
		}
		
		// Email is taken
		if (service != null && service.findByEmail(user.getEmail()) != null) {
			errors.rejectValue("email", "TAKEN", "This email address has been taken.");
		}
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		this.validate(target, errors, null);
	}
}
