package propra2.leihOrDie.model;

import lombok.Data;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Data
@Entity
public class User {
    @Size(max=100)
    @Id
    @Column(name="username")
    private String username;
    private String email;
    private String password;

    @Embedded
    private Address address;

    public User() {}

    public User(String username, String email, String password, Address address) {
        this.username = username;
        this.email = email;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.address = address;
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean verifyPassword(String password) {
        return BCrypt.checkpw(password, this.password);
    }
}


