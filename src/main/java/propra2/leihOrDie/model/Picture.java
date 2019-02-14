package propra2.leihOrDie.model;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@IdClass(PrimaryKey.class)
public class Picture {
    @Id
    @ManyToOne
    @JoinColumn(name="item_id")
    private Item item;

    @Id
    @GeneratedValue
    @Column(name="picture_id")
    private Long id;

    public Picture() {}

    public Picture(Item item) {
        /*if(id >= 10) {
            throw Exception;
        }*/

        this.item = item;
    }
}

class PrimaryKey implements Serializable {
    protected Long id;

    protected Item item;

    public PrimaryKey() {}

    public PrimaryKey(Item item) {
        this.item = item;
    }
}
