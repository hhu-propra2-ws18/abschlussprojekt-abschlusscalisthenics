package propra2.leihOrDie.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
public class Loan {
    private String state;
    private int duration;

    @ManyToOne
    @JoinColumn(name="username")
    private User user;

    @ManyToOne
    @JoinColumn(name="item_id")
    private Item item;
}
