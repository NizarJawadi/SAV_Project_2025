package com.sav.authentification.controller;

import java.util.List;
import java.util.Map;

import com.sav.authentification.model.Roles;
import com.sav.authentification.model.Technicien;
import com.sav.authentification.model.User;
import com.sav.authentification.services.TechnicienServiceImpl;
import com.sav.authentification.services.UserServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("User")
public class UserController {
	
	@Autowired 
	private UserServicesImpl userService ;


	
	
	@PostMapping(path = "user")
	void addUser (@RequestBody User user) {
		userService.addUser(user);
		}
	
	
    //@Secured("ADMIN") // Autoriser uniquement les utilisateurs avec le rôle "ROLE_USER"
	@GetMapping(path = "users")
	List<User> getAllUser() {
		return userService.getAllUsers();
	}
	
	@GetMapping("/user/{id}")
	User getUserById(@PathVariable Long id ) {
		return userService.getUserById(id);
	}
	
	@DeleteMapping("/remove/{id}")
	void deleteUser (@PathVariable Long id) {
		userService.deleteUser(id);
		}


	@GetMapping("/stats")
	public Map<String, Integer> getStats() {
		return  userService.getStats();
	}

}

