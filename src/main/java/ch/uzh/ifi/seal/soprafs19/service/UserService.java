package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.catalina.connector.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Iterable<User> getUsers() {
        return this.userRepository.findAll();
    }

    public ResponseEntity<User> createUser(User newUser) {
        Optional<User> newName = userRepository.findByUsername(newUser.getUsername());
        if(newName.isPresent()){
            return ResponseEntity.notFound().build();
        }
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.ONLINE);
        newUser.setDate(new Date());
        userRepository.save(newUser);
        return ResponseEntity.ok(newUser);
    }


    public ResponseEntity<User> authenticateUser(User possibleUser) {
        Optional<User> potentialUser = userRepository.findByUsername(possibleUser.getUsername());
        if(potentialUser.isPresent()) {
            if(potentialUser.get().getPassword().equals(possibleUser.getPassword())) {
                return ResponseEntity.ok(potentialUser.get());
            }
        }
        return ResponseEntity.notFound().build();
    }
}