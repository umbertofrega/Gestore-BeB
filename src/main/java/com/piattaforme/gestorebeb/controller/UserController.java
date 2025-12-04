package com.piattaforme.gestorebeb.controller;

import com.piattaforme.gestorebeb.model.entities.User;
import com.piattaforme.gestorebeb.model.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
     this.userService=userService;
    }

    //Create

    //Read
    @PreAuthorize("hasRole('RECEPTIONIST') or hasRole('OWNER')")
    @GetMapping
    public ResponseEntity<?> getAllUsers(){
        List<User> users = userService.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

}
