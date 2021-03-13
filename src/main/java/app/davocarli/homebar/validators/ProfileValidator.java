package app.davocarli.homebar.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import app.davocarli.homebar.models.User;

@Component
public class ProfileValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		User user = (User)target;
		
		if (user.getFirstName().length() < 1) {
			errors.rejectValue("firstName", "LENGTH");
		}
		if (user.getLastName().length() < 1) {
			errors.rejectValue("lastName", "LENGTH");
		}
		
	}
}
