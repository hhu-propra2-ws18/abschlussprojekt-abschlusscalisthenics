package propra2.leihOrDie.model;

import lombok.Data;
import javax.persistence.*;
import java.util.Currency;

@Data
@Entity
public class Item {
    @Id
    @GeneratedValue
    @Column(name="item_id")
    private Long id;

    private String name;
    private String description;
    private Currency cost;
    private Currency deposit;
    private boolean availability;
    private int availableTime;

    @ManyToOne
    @JoinColumn(name="username")
    private User user;

}
