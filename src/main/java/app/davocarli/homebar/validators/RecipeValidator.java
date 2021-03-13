package app.davocarli.homebar.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import app.davocarli.homebar.models.Ingredient;
import app.davocarli.homebar.models.Recipe;

@Component
public class RecipeValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Ingredient.class.equals(clazz);
	}
	
	public void validate(Object target, Errors errors) {
		Recipe recipe = (Recipe)target;
		
		if (recipe.getName().length() < 5) {
			errors.rejectValue("name", "LENGTH");
		}
		if (recipe.getInstructions().length() < 10) {
			errors.rejectValue("instructions", "LENGTH");
		}
		if (recipe.getCreator() == null) {
			errors.rejectValue("creator", "CREATOR");
		}
	}
	
}
