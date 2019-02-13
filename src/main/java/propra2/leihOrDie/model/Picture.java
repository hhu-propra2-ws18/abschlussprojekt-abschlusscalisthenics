package propra2.leihOrDie.model;

import lombok.Data;
import javax.persistence.*;
import javax.sql.rowset.serial.SerialBlob;

@Data
@Entity
public class Picture {
    @Id
    @GeneratedValue
    @Column(name="picture_id")
    private Long id;

    private SerialBlob blob;

    @ManyToOne
    @JoinColumn(name="item_id")
    private Item item;

    public Picture(SerialBlob blob, Item item) {
        this.blob = blob;
        this.item = item;
    }
}
