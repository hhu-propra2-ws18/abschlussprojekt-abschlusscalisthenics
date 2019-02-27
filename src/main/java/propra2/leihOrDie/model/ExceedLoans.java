package propra2.leihOrDie.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class ExceedLoans {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="buy_id")
    private Long id;

    @OneToOne
    @JoinColumn(name="username")
    private User borrower;

    @OneToOne
    private User lender;

    @ManyToOne
    @JoinColumn(name="item_id")
    private Item item;

    public ExceedLoans() {}

    public ExceedLoans(User borrower, User lender, Item item) {
        this.borrower = borrower;
        this.lender = lender;
        this.item = item;
    }
}
