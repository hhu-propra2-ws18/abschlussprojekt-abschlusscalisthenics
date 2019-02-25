package propra2.leihOrDie.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="transaction_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="username")
    private User fromUser;

    @ManyToOne
    @JoinColumn(name="username")
    private User toUser;

    private double amount;
    private String comment;
    private Date date;

    public Transaction() {}

    public Transaction(User fromUser, User toUser, double amount, String comment, Date date) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.amount = amount;
        this.comment = comment;
        this.date = date;
    }
}
