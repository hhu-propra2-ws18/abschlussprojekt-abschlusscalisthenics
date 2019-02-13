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
    private int cost;
    private int deposit;
    private boolean availability;
    private int availableTime;
    private String location;

    @ManyToOne
    @JoinColumn(name="username")
    private User user;

}
