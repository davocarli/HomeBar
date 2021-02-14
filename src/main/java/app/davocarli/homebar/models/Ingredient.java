package app.davocarli.homebar.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name="ingredients")
public class Ingredient {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Size(min=1, message="Ingredient name cannot be empty.")
	private String ingredientName;
	
	private String substituteNames;
	
	public Ingredient() {}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getIngredientName() {
		return ingredientName;
	}
	public void setIngredientName(String ingredientName) {
		this.ingredientName = ingredientName;
	}
	
	public String getSubstituteNames() {
		return substituteNames;
	}
	public void setSubstituteNames(String substituteNames) {
		this.substituteNames = substituteNames;
	}
}
