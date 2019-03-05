package ch.uzh.ifi.seal.soprafs19.controller;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.exception.UserAlreadyExists;
import ch.uzh.ifi.seal.soprafs19.exception.UserNotFound;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
public class UserController {

    private final UserService service;
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    UserController(UserService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/users/{userId}")
    public ResponseEntity<User> updateUsers(@PathVariable Long userId, @RequestBody User possibleUser) throws UserNotFound{
        if(userId.equals(possibleUser.getId()) ){
            return this.service.updateUser(possibleUser);
        }
        throw new UserNotFound("user ID does not map.");
    }

    @GetMapping("/users")
    Iterable<User> all() {
        return service.getUsers();
    }

    @PostMapping("/login")
    public ResponseEntity<User> authenticate(@RequestBody User possibleUser) throws UserNotFound {

        return this.service.authenticateUser(possibleUser);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/users/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long userId) throws UserNotFound {
        return this.service.showUser(userId);
    }

    @PostMapping("/users")
    public ResponseEntity<User> registerUser(@RequestBody User newUser) throws UserAlreadyExists {

        return this.service.createUser(newUser);
    }
}

