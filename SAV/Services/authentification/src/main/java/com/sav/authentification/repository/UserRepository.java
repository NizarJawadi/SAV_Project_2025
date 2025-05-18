package com.sav.authentification.repository;

import com.sav.authentification.model.Roles;
import com.sav.authentification.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {

	User findUserByLogin(String login);

	User findUserByIdUser(Long idUser);

	int countUserByRole(Roles role);

	boolean existsByNumSIP(String numSIP);

	List<User> findByNumSIPIsNotNull();
}
