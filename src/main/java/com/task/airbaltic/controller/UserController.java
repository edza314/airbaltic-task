package com.task.airbaltic.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.task.airbaltic.domain.Users;
import com.task.airbaltic.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.task.airbaltic.repository.UsersRepository;


@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UsersRepository usersRepository;

    // get all users
    @GetMapping("/users")
    public List<Users> getAllUsers(){
        return usersRepository.findAll();
    }

    // create users rest api
    @PostMapping("/users")
    public Users createUser(@RequestBody Users users) {
        return usersRepository.save(users);
    }

    // get users by id rest api
    @GetMapping("/users/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not exist with id :" + id));
        return ResponseEntity.ok(user);
    }

    // update users rest api

    @PutMapping("/users/{id}")
    public ResponseEntity<Users> updateUsers(@PathVariable Long id, @RequestBody Users usersDetails){
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not exist with id :" + id));

        user.setFirstName(usersDetails.getFirstName());
        user.setLastName(usersDetails.getLastName());
        user.setEmailId(usersDetails.getEmailId());

        Users updatedUser = usersRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    // delete users rest api
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteUsers(@PathVariable Long id){
        Users users = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not exist with id :" + id));

        usersRepository.delete(users);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}