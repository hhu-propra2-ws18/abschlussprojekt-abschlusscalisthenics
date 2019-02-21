package propra2.leihOrDie.web;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class LoanForm {
    @NotNull
    @Min(1)
    private int availableTime;

    @NotNull
    @Min(1)
    private int loanDuration;
}
