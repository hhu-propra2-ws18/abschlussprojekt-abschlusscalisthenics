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
    private char[] password;

}
