package propra2.leihOrDie.model;

import lombok.Data;
import javax.persistence.*;
import java.util.List;

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

    @OneToMany
    private List<Picture> pictures;

    @OneToMany
    private List<Loan> loans;

    public Item(String name, String description, int cost, int deposit, boolean availability, int availableTime,
                String location, User user) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.deposit = deposit;
        this.availability = availability;
        this.availableTime = availableTime;
        this.location = location;
        this.user = user;
    }

}
