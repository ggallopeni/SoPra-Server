package ch.uzh.ifi.seal.soprafs19.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.CONFLICT)
public class UserAlreadyExists extends Exception{
    public UserAlreadyExists(String message){
        super(message);
    }
}