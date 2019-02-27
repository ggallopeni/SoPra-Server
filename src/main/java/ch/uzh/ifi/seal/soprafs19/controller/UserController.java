package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
public class UserController {

    private final UserService service;

    UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/login")
    Iterable<User> all() {
        return service.getUsers();
    }

    @PostMapping("/login")
    public ResponseEntity<User> authenticate(@RequestBody User possibleUser) {
        return this.service.authenticateUser(possibleUser);
    }

    /*@GetMapping("/register")
    Iterable<User> all_users() {
        return service.getUsers();
    }*/

    @PostMapping("/loginregister")
    public ResponseEntity<User> registerUser(@RequestBody User newUser) {
        return this.service.createUser(newUser);
    }
}

