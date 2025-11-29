package com.piattaforme.gestorebeb.model.services;

import com.piattaforme.gestorebeb.model.entities.User;
import com.piattaforme.gestorebeb.model.exceptions.conflict.EmailAlreadyExists;
import com.piattaforme.gestorebeb.model.repositories.UserRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public User registerUser(User user) {
        if(user!=null && !userRepository.existsByEmail(user.getEmail()))
            return userRepository.save(user);
        throw new EmailAlreadyExists("User already exists");
    }

    @Transactional
    public User syncUserWithKeycloak() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();

        String email = jwt.getClaim("email");
        String name = jwt.getClaim("name");

        User user = userRepository.findByEmail(email);

        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setName(name);
            user = userRepository.save(user);
        } else {
            if (user.getName() == null) {
                user.setName(name);
                user = userRepository.save(user);
            }
        }

        return user;
    }

    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
