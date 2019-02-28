package propra2.leihOrDie.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="item_id")
    private Long id;

    private String name;
    private String description;
    private int cost;
    private int deposit;
    private boolean availability;
    private int availableTime;
    private Address address;
    private int soldPrice;

    @ManyToOne
    @JoinColumn(name="username")
    private User user;

    public Item() {}

    public Item(String name, String description, int cost, int deposit, boolean availability, int availableTime,
                Address address, User user, int soldPrice) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.deposit = deposit;
        this.availability = availability;
        this.availableTime = availableTime;
        this.address = address;
        this.user = user;
        this.soldPrice = soldPrice;
    }
}
