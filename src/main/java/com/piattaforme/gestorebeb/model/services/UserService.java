package com.piattaforme.gestorebeb.model.services;

import com.piattaforme.gestorebeb.model.entities.User;
import com.piattaforme.gestorebeb.model.repositories.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        throw new IllegalArgumentException("User already exists");
    }

}
