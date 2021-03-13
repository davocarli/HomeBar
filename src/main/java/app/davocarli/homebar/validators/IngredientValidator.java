package app.davocarli.homebar.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import app.davocarli.homebar.models.Ingredient;

@Component
public class IngredientValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Ingredient.class.equals(clazz);
	}
	
	public void validate(Object target, Errors errors) {
		Ingredient ingredient = (Ingredient)target;
		
		// Name is < 3 characters
		if (ingredient.getName().length() < 3) {
			errors.rejectValue("name", "LENGTH");
		}
		String status = ingredient.getStatus();
		if (status != "stock" && status != "shop" && status != "recipe") {
			errors.rejectValue("status", "INVALID");
		}
	}
	
	public boolean validate(Object target) {
		Ingredient ingredient = (Ingredient)target;
		
		String status = ingredient.getStatus();
		
		if (ingredient.getName().length() < 3 ||
			(
				status != "stock" &&
				status != "shop" &&
				status != "recipe"
					)	
			) {
			return false;
		}
		return true;
	}
}
