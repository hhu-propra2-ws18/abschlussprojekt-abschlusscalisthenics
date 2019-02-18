package propra2.leihOrDie.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class Picture {
    @ManyToOne
    @JoinColumn(name="item_id")
    private Item item;

    @Id
    @Column(name="picture_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Picture() {}

    public Picture(Item item) {
        this.item = item;
    }
}

