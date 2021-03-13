package app.davocarli.homebar.services;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import app.davocarli.homebar.models.User;
import app.davocarli.homebar.repositories.UserRepository;

@Service
public class UserService {
	private final UserRepository repo;
	
	public UserService(UserRepository repo) {
		this.repo = repo;
	}
	
	// Get All Users
	public List<User> getAll() {
		return repo.findAll();
	}
	
	// Get by username
	public User findByUsername(String username) {
		return repo.findByUsername(username);
	}
	
	// Register the user and generate a hash of their pwd
	public User registerUser(User user) {
		String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
		user.setPassword(hashed);
		return repo.save(user);
	}
	
	// Get user by email
	public User findByEmail(String email) {
		return repo.findByEmail(email);
	}
	
	// Get user by ID
	public User findById(Long id) {
		return repo.findById(id).orElse(null);
	}
	
	// Authenticate user
	public User authenticate(String email, String password) {
		User user = repo.findByEmail(email);
		if (user != null && BCrypt.checkpw(password, user.getPassword())) {
			return user;
		} else {
			return null;
		}
	}
	
	// Update User
	public User updateUser(User user) {
		return repo.save(user);
	}
	
	// Delete user
	public void deleteUser(User user) {
		repo.delete(user);
	}
}
