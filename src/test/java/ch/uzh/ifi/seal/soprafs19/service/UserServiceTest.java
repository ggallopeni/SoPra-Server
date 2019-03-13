package ch.uzh.ifi.seal.soprafs19.service;
import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.entity.UserDTO;
import ch.uzh.ifi.seal.soprafs19.exception.UserAlreadyExists;
import ch.uzh.ifi.seal.soprafs19.exception.UserNotFound;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import java.util.Date;
import java.util.Optional;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class UserServiceTest {


    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test(expected = UserAlreadyExists.class)
    public void createUser() throws UserAlreadyExists {
        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPw");
        testUser.setBirthday(new Date());

        ResponseEntity<User> creatUser = userService.createUser(testUser);

        Assert.assertEquals("########## Status code not 200! Test failed.",200, creatUser.getStatusCodeValue());

        Optional<User> optUser = userRepository.findById(testUser.getId());

        //check all attributes got assigned correctly
        Assert.assertNotNull("########## Token must not be null!",optUser.get().getToken());
        Assert.assertNotNull(optUser.get().getCreationDate());
        Assert.assertEquals(optUser.get().getStatus(), UserStatus.OFFLINE);
        Assert.assertEquals(optUser, userRepository.findByToken(optUser.get().getToken()));

        //check correct status for existing username: 409
        User existingUser = new User();
        existingUser.setName("testName");
        existingUser.setUsername("testUsername");
        existingUser.setPassword("testPw");
        existingUser.setBirthday(new Date());
        Assert.assertEquals(409, userService.createUser(existingUser).getStatusCodeValue());
    }


    @Test(expected = UserNotFound.class)
    public void updateUser() throws UserNotFound, UserAlreadyExists {
        //User to be changed
        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setBirthday(new Date());

        ResponseEntity<User> effectiveUser = userService.createUser(testUser);

        // Changes coming from frontend
        UserDTO newUser = new UserDTO();
        newUser.setUsername("newTestUsername");
        newUser.setName("newTestName");
        newUser.setPassword("newTestPw");


        ResponseEntity updatedUser = userService.updateUser(testUser.getId(), newUser);

        //check whether names got changed
        Assert.assertEquals("######### Names do not match! ",userRepository.findById(testUser.getId()).get().getName(), newUser.getName());
        Assert.assertEquals("######### UserNames do not match! ", userRepository.findById(testUser.getId()).get().getUsername(), newUser.getUsername());
        Assert.assertEquals("######### Passwords do not match! ",userRepository.findById(testUser.getId()).get().getPassword(), newUser.getPassword());

        //check whether status is right
        Assert.assertEquals(200, updatedUser.getStatusCodeValue());


        //Case: user id not found
        Long inexistent = 999L;
        //ResponseEntity inexistentUser = userService.updateUser(inexistent, newUser);


        Assert.assertEquals(404, userService.updateUser(inexistent, newUser).getStatusCodeValue());
    }

    /*@Test(expected = UserNotFound.class)
    public void testUserNotFound(){
        Long inexistent = 999L;
        userService.updateUser(inexistent, newUser);
    }*/
}
