package com.sav.authentification.services;

import java.util.ArrayList;
import java.util.List;

import com.sav.authentification.model.Roles;
import com.sav.authentification.model.User;
import com.sav.authentification.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServicesImpl implements UserServices{

	private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    
    @Autowired
    public UserServicesImpl(@Lazy UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.repository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
	
	@Override
	public void addUser(User u) {
		u.setPassword(passwordEncoder.encode(u.getPassword()));
		repository.save(u);
		}

	@Override
	public User getUserById(Long id) {
		return repository.findUserByIdUser(id);
	}

	@Override
	public List<User> getAllUsers() {
		return repository.findAll();
	}

	@Override
	public void deleteUser(Long id) {
		repository.deleteById(id);
		
	}

	 @Override
	    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
	        User user = repository.findUserByLogin(login);
	        if (user == null) {
	            throw new UsernameNotFoundException("User not found");
	        }
	        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), new ArrayList<>());
	    }

	@Override
	public Roles getRoleByLogin(String login) {
		User us = repository.findUserByLogin(login) ;
		 
			return us.getRole() ;
		
	}

	@Override
	public User getUserByLogin(String login) {
	// TODO Auto-generated method stub
		return repository.findUserByLogin(login);
	}

}
