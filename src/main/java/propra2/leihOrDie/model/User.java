package propra2.leihOrDie.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import javax.persistence.*;
import javax.validation.constraints.Size;

import propra2.leihOrDie.WebSecurityConfig;
import org.springframework.security.crypto.password.PasswordEncoder;


@Data
@Entity
public class User {
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Size(max=100)
    @Id
    @Column(name="username")
    private String username;

    private String email;
    private String password;

    @Embedded
    private Address address;

    public User() {}

    public User(String username, String email, CharSequence password, Address address) {
        this.username = username;
        this.email = email;
        this.password = passwordEncoder.encode(password);
        this.address = address;
    }

    public void setPassword(String password) {
        this.password = passwordEncoder.encode(password);
    }
}


