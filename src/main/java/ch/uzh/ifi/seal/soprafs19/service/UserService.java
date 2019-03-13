package ch.uzh.ifi.seal.soprafs19.service;
import ch.uzh.ifi.seal.soprafs19.entity.UserDTO;
import ch.uzh.ifi.seal.soprafs19.exception.UserAlreadyExists;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.exception.UserNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public ResponseEntity logout(String logoutToken) throws UserNotFound {
        Optional<User> opLogoutUser = userRepository.findByToken(logoutToken);
        if(opLogoutUser.isPresent()){
            userRepository.findByToken(logoutToken).get().setStatus(UserStatus.OFFLINE);
            userRepository.save(opLogoutUser.get());
            return ResponseEntity.ok("");
        }
        throw new UserNotFound("Logout user failed: token not found.");
    }

    public ResponseEntity<User> createUser(User newUser) throws UserAlreadyExists {
        Optional<User> newName = userRepository.findByUsername(newUser.getUsername());
        if(newName.isPresent()){
            throw new UserAlreadyExists("Following username is already given" + newName.get().getUsername());

        }
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.OFFLINE); // users need to login after first registration
        newUser.setCreationDate(new Date());
        userRepository.save(newUser);
        //System.out.println(newUser.getId());
        return ResponseEntity.ok().body(newUser);
    }


    public ResponseEntity updateUser(Long uID, UserDTO user) throws UserNotFound{
        Optional<User> user2Update = userRepository.findById(uID);
        if(user2Update.isPresent()){
            if(user.getName() != null){
                user2Update.get().setName(user.getName());
            }
            if(user.getPassword() != null){
                user2Update.get().setPassword(user.getPassword());
            }
            if(user.getUsername() != null){
                user2Update.get().setUsername(user.getUsername());
            }
            userRepository.save(user2Update.get());
            return ResponseEntity.ok("");
        }
        throw new UserNotFound("Failed to update user in Service: user not present. See ID:   " + uID);
    }


    public ResponseEntity<User> showUser(Long id) throws UserNotFound {
        return ResponseEntity.ok().body(userRepository.findById(id).orElseThrow(() -> new UserNotFound("User with following ID not found:" + id)));
    }


    public ResponseEntity<User> authenticateUser(User possibleUser) throws UserNotFound {
        User potentialUser = userRepository.findByUsername(possibleUser.getUsername()).orElseThrow(() -> new UserNotFound("User with following username not found : " + possibleUser.getUsername()));
        if (potentialUser.getPassword().equals(possibleUser.getPassword())){
        potentialUser.setStatus(UserStatus.ONLINE);
        return ResponseEntity.ok().body(potentialUser);
        }
        throw new UserNotFound("Password does not match.");
    }
}