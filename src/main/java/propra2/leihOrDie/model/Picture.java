package propra2.leihOrDie.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@IdClass(Picture.ComposedKey.class)
public class Picture {
    @Id
    @ManyToOne
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JoinColumn(name="item_id")
    private Item item;

    @Id
    @Column(name="picture_id")
    private Long id;

    public Picture() {}

    public Picture(Item item) {
        this.item = item;
    }

    static class ComposedKey implements Serializable {
        protected Long id;

        protected Item item;

        public ComposedKey() {}

        public ComposedKey(Item item) {
            this.item = item;
        }
    }
}
