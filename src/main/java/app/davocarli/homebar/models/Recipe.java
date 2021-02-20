package app.davocarli.homebar.models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="recipes")
public class Recipe {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Size(min=5, message="Drink Name must be at least 50 characters.")
	private String name;
	
	@Size(min=10, message="Please include at least 10 characters.")
	private String instructions;
	
	@Column(updatable=false)
	private Date createdAt;
	private Date updatedAt;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(
			name="recipe_ingredients",
			joinColumns=@JoinColumn(name="recipe_id"),
			inverseJoinColumns=@JoinColumn(name="ingredient_id")
	)
	private List<Ingredient> recipeIngredients;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_created")
	private User creator;
	
	@OneToMany(mappedBy="recipe", fetch=FetchType.LAZY)
	@JsonIgnore
	private List<Rating> ratings;
	
	private Long averageRating;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getInstructions() {
		return instructions;
	}
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	
	@PrePersist
	protected void beforeCreate() {
		this.createdAt = new Date();
	}
	@PreUpdate
	protected void beforeUpdate() {
		this.updatedAt = new Date();
	}
	
	public List<Ingredient> getRecipeIngredients() {
		return recipeIngredients;
	}
	public User getCreator() {
		return creator;
	}
	
	public Long getAverageRating() {
		return averageRating;
	}
	public void setAverageRating(Long rating) {
		this.averageRating = rating;
	}
}
