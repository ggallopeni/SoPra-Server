package ch.uzh.ifi.seal.soprafs19.entity;
import java.text.SimpleDateFormat;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false, unique = true) 
	private String username;
	
	@Column(nullable = false, unique = true) 
	private String token;

	@Column(nullable = false)
	private UserStatus status;

	@DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
	@Column(nullable = false)
	private Date creationDate;


	@Column(nullable = false)
	//@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date birthday;


	public Long getId() {
		return id;
	}

	public void setId(Long id) { this.id = id; }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}



	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserStatus getStatus() {
		return status;
	}

	public Date getBirthday() {
		return birthday;
	}

	public Date getDate() {
		return creationDate;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public void setDate(Date registerDate){
		this.creationDate = registerDate;
	}

	public void setBirthday(Date registerBirthday){
		this.birthday = registerBirthday;
	}



	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof User)) {
			return false;
		}
		User user = (User) o;
		return this.getId().equals(user.getId());
	}
}
