package propra2.leihOrDie.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
public class Loan {
    @Id
    @GeneratedValue
    @Column(name="loan_id")
    private Long id;

    private String state;
    private int duration;

    @ManyToOne
    @JoinColumn(name="username")
    private User user;

    @ManyToOne
    @JoinColumn(name="item_id")
    private Item item;

    public Loan(String state, int duration, User user, Item item) {
        this.state = state;
        this.duration = duration;
        this.user = user;
        this.item = item;
    }
}
