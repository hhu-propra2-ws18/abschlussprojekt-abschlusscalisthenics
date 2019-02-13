package propra2.leihOrDie.model;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Entity
public class User {
    @Size(max=100)
    @Id
    @Column(name="username")
    private String username;

    private String email;
    private byte[] password;

    @OneToMany
    private List<Item> items;

    @OneToMany
    private List<Loan> loans;

    public User(String username, String email, byte[] password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

}
