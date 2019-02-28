package propra2.leihOrDie.web;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class BuyForm {
    @NotNull
    @Min(1)
    private int purchasePrice;
}
