package propra2.leihOrDie.model;

import lombok.Data;
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
    private byte[] password;

    public User(String username, String email, byte[] password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

}
