package app.davocarli.homebar.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
	
	// Substitute Names will be a "\n" separated list
	private String substituteNames;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(
			name="users_stock",
			joinColumns=@JoinColumn(name="ingredient_id"),
			inverseJoinColumns=@JoinColumn(name="user_id")
	)
	private List<User> stockedUsers;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(
			name="users_shopping",
			joinColumns=@JoinColumn(name="ingredient_id"),
			inverseJoinColumns=@JoinColumn(name="user_id")
	)
	private List<User> shoppingUsers;
	
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
	
	public String getFullIngredient() {
		return ingredientName + "\n" + substituteNames;
	}
}
