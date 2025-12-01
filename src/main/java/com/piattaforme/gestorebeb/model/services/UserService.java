package com.piattaforme.gestorebeb.model.services;

import com.piattaforme.gestorebeb.model.entities.User;
import com.piattaforme.gestorebeb.model.exceptions.notFound.UserNotFoundException;
import com.piattaforme.gestorebeb.model.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;


    //Uso il costruttore perché è la versione di injection più moderna
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User syncUserWithKeycloak() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new IllegalArgumentException("Authentication must be a JWT token.");
        }

        String code = jwt.getSubject();

        User user = userRepository.findByCode(code);

        if (user == null)
            throw new UserNotFoundException("The user doesn't exist");

        return user;
    }

    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
