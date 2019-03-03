package ch.uzh.ifi.seal.soprafs19.service;
import ch.uzh.ifi.seal.soprafs19.exception.UserAlreadyExists;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.exception.UserNotFound;
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



    public ResponseEntity<User> createUser(User newUser) throws UserAlreadyExists {
        Optional<User> newName = userRepository.findByUsername(newUser.getUsername());
        if(newName.isPresent()){

            throw new UserAlreadyExists("Following username is already given" + newName.get().getUsername());
            //return ResponseEntity.notFound().build();
        }
        newUser.setToken(UUID.randomUUID().toString());

        newUser.setStatus(UserStatus.OFFLINE); // users need to login after first registration
        newUser.setDate(new Date());
        userRepository.save(newUser);
        return ResponseEntity.ok().body(newUser);
    }


    public ResponseEntity<User> showUser(Long id) throws UserNotFound {
        return ResponseEntity.ok().body(userRepository.findById(id).orElseThrow(() -> new UserNotFound("User with following ID not found:" + id)));
    }


    public ResponseEntity<User> authenticateUser(User possibleUser) throws UserNotFound {
        User potentialUser = userRepository.findByUsername(possibleUser.getUsername()).orElseThrow(() -> new UserNotFound("User with following username not found : " + possibleUser.getUsername()));
        potentialUser.setStatus(UserStatus.ONLINE);
        return ResponseEntity.ok().body(potentialUser);
    }
}