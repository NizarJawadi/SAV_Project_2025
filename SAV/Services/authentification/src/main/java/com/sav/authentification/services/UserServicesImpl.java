package com.sav.authentification.services;

import java.util.*;
import java.util.stream.Collectors;

import com.sav.authentification.model.Roles;
import com.sav.authentification.model.User;
import com.sav.authentification.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServicesImpl implements UserServices{

	private static final int SIP_MIN = 7001;
	private static final int SIP_MAX = 7999;

	@Autowired
	private  UserRepository repository;
	@Autowired
    private  PasswordEncoder passwordEncoder;
	@Autowired
	private  UserRepository userRepository;


	public String generateUniqueSIPNumber() {
		// Récupérer tous les numéros SIP existants
		Set<String> existingSIPs = userRepository.findByNumSIPIsNotNull()
				.stream()
				.map(User::getNumSIP)
				.collect(Collectors.toSet());

		// Générer un numéro aléatoire jusqu'à en trouver un non utilisé
		Random random = new Random();
		String generatedSIP;

		do {
			int randomNum = SIP_MIN + random.nextInt(SIP_MAX - SIP_MIN + 1);
			generatedSIP = String.valueOf(randomNum);
		} while (existingSIPs.contains(generatedSIP) || generatedSIP.length() != 4);

		return generatedSIP;
	}

	@Transactional
	public User saveUserWithSIP(User user) {
		if (user.getNumSIP() == null || user.getNumSIP().isEmpty()) {
			String sip;
			do {
				sip = generateUniqueSIPNumber();
			} while (userRepository.existsByNumSIP(sip));

			user.setNumSIP(sip);
		}
		return userRepository.save(user);
	}
	/*@Autowired
    public UserServicesImpl(@Lazy UserRepository userRepository, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.repository = userRepository;
        this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
	}*/
	
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


	public Map<String, Integer> getStats() {
		int totalClients = userRepository.countUserByRole(Roles.CLIENT);
		int totalTechniciens = userRepository.countUserByRole(Roles.TECHNICIEN);
		int totalResponsables = userRepository.countUserByRole(Roles.RESPONSABLE_SAV);

		Map<String, Integer> stats = new HashMap<>();
		stats.put("totalClients", totalClients);
		stats.put("totalTechniciens", totalTechniciens);
		stats.put("totalResponsables", totalResponsables);

		return stats;
	}


	public String getUserSipNumber(Long id) {
		return repository.findUserByIdUser(id).getNumSIP();
	}

}
