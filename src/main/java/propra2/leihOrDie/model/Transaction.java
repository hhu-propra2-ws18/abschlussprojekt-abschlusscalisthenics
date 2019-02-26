package propra2.leihOrDie.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private LocalDateTime date;

    public Transaction() {}

    public Transaction(User fromUser, User toUser, double amount, String comment) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.amount = amount;
        this.comment = comment;
        this.date = LocalDateTime.now();
    }
}
