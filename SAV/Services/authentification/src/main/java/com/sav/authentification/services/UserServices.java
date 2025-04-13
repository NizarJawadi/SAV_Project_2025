package com.sav.authentification.services;

import java.util.List;

import com.sav.authentification.model.Roles;
import com.sav.authentification.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;



public interface UserServices extends UserDetailsService{

	public void addUser(User u);

	public User getUserById(Long id);

	public List<User> getAllUsers();

	public void deleteUser(Long id);
	
	public Roles getRoleByLogin(String login ) ;
	
	public User getUserByLogin(String login);


}
