package propra2.leihOrDie.model;

import lombok.Data;

import java.util.List;

@Data
public class Account {
    private String account;
    private double amount;
    private List<Reservation> reservations;
}
