package propra2.leihOrDie.web;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class TransactionForm {
    @Min(value = 1, message = "Der Mindestbetrag beläuft sich auf einen Euro!")
    private double amount;
}
