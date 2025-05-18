package com.sav.authentification.repository;

import com.sav.authentification.model.Client;
import com.sav.authentification.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email);

    boolean existsByNumSIP(String numSIP);

    List<User> findByNumSIPIsNotNull();
}
