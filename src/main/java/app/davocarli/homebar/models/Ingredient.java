package app.davocarli.homebar.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name="ingredients")
public class Ingredient {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Size(min=1, message="Ingredient name cannot be empty.")
	private String name;
	
	// Substitute Names will be a "\n" separated list
	private String substituteNames;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="recipe_id")
	private Recipe recipe;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	
	// Used to identify how this ingredient is being used, to avoid needing an additional
	// model. Status should always be one of "stock", "shop", or "recipe".
	private String status;
	
	private String amount;
	
	public Ingredient() {}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String ingredientName) {
		this.name = ingredientName;
	}
	
	public String getSubstituteNames() {
		return substituteNames;
	}
	public void setSubstituteNames(String substituteNames) {
		this.substituteNames = substituteNames;
	}
	
	public String getFullIngredient() {
		return name + "|" + substituteNames;
	}
	
	public Recipe getRecipe() {
		return recipe;
	}
	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getStockedList() {
		if (status.equals("stock")) {
			return getFullIngredient();
		} else {
			return "";
		}
	}
	
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
}
