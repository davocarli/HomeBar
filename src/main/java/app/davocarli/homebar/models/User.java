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
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="users")
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Size(min=1, message="First name cannot be empty.")
	private String firstName;
	@Size(min=1, message="Last name cannot be empty.")
	private String lastName;
	
	@Size(min=4, message="Username must be at least 4 characters.")
	@Column(unique=true)
	private String username;
	
	@Email
	@Column(unique=true)
	private String email;
	
	private String password;
	@Transient
	private String passwordConfirmation;
	
	@Column(updatable=false)
	private Date createdAt;
	private Date updatedAt;
	
	@OneToMany(mappedBy="user", fetch=FetchType.LAZY)
	private List<Ingredient> ingredients;
		
	@OneToMany(mappedBy="user", fetch=FetchType.LAZY)
	@JsonIgnore
	private List<Rating> ratings;
	
	public User() {}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getFullName() {
		return firstName + " " + lastName;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}
	public void setPasswordConfirmation(String password) {
		this.passwordConfirmation = password;
	}
	
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public Date getCreatedAt() {
		return createdAt;
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
	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}
	
	public String getFullStock() {
		return ingredients.stream().map(Ingredient::getStockedList).collect(Collectors.joining("|")).replace("||", "|");
	}
	
	public List<Rating> getRatings() {
		return ratings;
	}
	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}
}
