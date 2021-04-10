package app.davocarli.homebar.models;

import java.util.ArrayList;
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
	
	@Size(min=5, message="Drink Name must be at least 5 characters.")
	private String name;
	
	@Size(min=10, message="Please include at least 10 characters.")
	private String instructions;
	
	@Column(updatable=false)
	private Date createdAt;
	private Date updatedAt;
	
	@Column(nullable=true, length=100)
	private String image;
	
	@OneToMany(mappedBy="recipe", fetch=FetchType.LAZY)
	private List<Ingredient> ingredients;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_created")
	private User creator;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(
			name="recipes_favorites",
			joinColumns=@JoinColumn(name="recipe_id"),
			inverseJoinColumns=@JoinColumn(name="user_id")
			)
	private List<User> favorited;
	
	@OneToMany(mappedBy="recipe", fetch=FetchType.LAZY)
	@JsonIgnore
	private List<Rating> ratings;
	
	private String source;
	
	private Double ordering;
	
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
	public void updateRating() {
		if (this.ratings.size() > 0) {
			Long sum = new Long(0);
			for (int i = 0; i < this.ratings.size(); i++) {
				sum += this.ratings.get(i).getRatingValue();
			}
			this.averageRating = sum / this.ratings.size();
		} else {
			this.averageRating = null;
		}
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
	
	public List<User> getFavorited() {
		return favorited;
	}
	public void setFavorited(List<User> favorited) {
		this.favorited = favorited;
	}
	
	public List<Rating> getRatings() {
		return ratings;
	}
	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}
	public Boolean isFavoritedBy(Long userId) {
		for (int i = 0; i < this.favorited.size(); i++) {
			if (this.favorited.get(i).getId().equals(userId)) {
				return true;
			}
		}
		return false;
	}
	public List<Long> getFavoritedUserIds() {
		List<Long> ids = new ArrayList<Long>();
		for (int i = 0; i < this.favorited.size(); i++) {
			User user = this.favorited.get(i);
			ids.add(user.getId());
		}
		return ids;
	}
	public Rating getRatingOfUser(Long userId) {
		for (int i = 0; i < this.ratings.size(); i++) {
			Rating rating = this.ratings.get(i);
			if (rating.getUser().getId().equals(userId)) {
				return rating;
			}
		}
		return null;
	}
	public Double getOrder() {
		return ordering;
	}
	public void setOrder(Double order) {
		this.ordering = order;
	}
}
