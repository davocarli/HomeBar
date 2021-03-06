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
			errors.rejectValue("passwordConfirmation", "MATCH");
		} 
		
		// Password is too short
		if (user.getPassword().length() < 8) {
			errors.rejectValue("password", "LENGTH");
		} 
		
		// First Name is blank
		if (user.getFirstName().length() < 1) {
			errors.rejectValue("firstName", "EMPTY");
		}
		
		// Last Name is blank
		if (user.getLastName().length() < 1) {
			errors.rejectValue("lastName", "EMPTY");
		}
		
		// Username is too short
		if (user.getUsername().length() < 4) {
			errors.rejectValue("username", "LENGTH");
		}
		
		// Username is taken
		if (service != null && service.findByUsername(user.getUsername()) != null) {
			errors.rejectValue("username", "TAKEN");
		}
		
		// Email is taken
		if (service != null && service.findByEmail(user.getEmail()) != null) {
			errors.rejectValue("email", "TAKEN");
		}
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		this.validate(target, errors, null);
	}
}
