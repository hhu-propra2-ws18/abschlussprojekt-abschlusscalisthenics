package propra2.leihOrDie.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
public class User {
    @Id
    @Column(name="username")
    private String username;

    private String email;
    private char[] password;

}
