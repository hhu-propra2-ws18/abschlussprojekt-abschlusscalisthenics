package propra2.leihOrDie.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Session {
    @Id
    private String sessionId;

    public Session() {}

    public Session(String sessionId) {
        this.sessionId = sessionId;
    }
}
