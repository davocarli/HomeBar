package app.davocarli.homebar.models;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
	
	@Column(nullable=true, length=64)
	private String image;
	
	@OneToMany(mappedBy="recipe", fetch=FetchType.LAZY)
	private List<Ingredient> ingredients;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_created")
	private User creator;
	
	@OneToMany(mappedBy="recipe", fetch=FetchType.LAZY)
	@JsonIgnore
	private List<Rating> ratings;
	
	private String source;
	
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
	
	public List<Ingredient> getIngredients() {
		return ingredients;
	}
	public User getCreator() {
		return creator;
	}
	public void setCreator(User user) {
		this.creator = user;
	}
	
	public Long getAverageRating() {
		return averageRating;
	}
	public void setAverageRating(Long rating) {
		this.averageRating = rating;
	}
	
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	
	public String getIngredientList() {
		return ingredients.stream().map(Ingredient::getName).collect(Collectors.joining(", "));
	}
	
	public String getAllFullIngredients() {
		return ingredients.stream().map(Ingredient::getFullIngredient).collect(Collectors.joining("\n"));
	}
	
}
