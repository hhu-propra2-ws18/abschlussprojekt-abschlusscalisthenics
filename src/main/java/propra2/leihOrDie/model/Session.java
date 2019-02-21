package propra2.leihOrDie.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Data
@Entity
public class Session {
    @Id
    private String sessionId;

    @OneToOne
    @JoinColumn(name="username")
    User user;

    public Session() {}

    public Session(String sessionId, User user) {
        this.sessionId = sessionId;
        this.user = user;
    }
}
