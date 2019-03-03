package ch.uzh.ifi.seal.soprafs19.repository;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("userRepository")
public interface UserRepository extends CrudRepository<User, Long> {
	List<User> findByName(String name);
	Optional<User> findByUsername(String username);
	User findByToken(String token);
	User findByPassword(String password);
	Optional<User> findById(Long id);




}
