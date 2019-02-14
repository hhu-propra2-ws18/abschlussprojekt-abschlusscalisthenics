package propra2.leihOrDie.web;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ItemForm {

   @NotNull
    @Size(min=2, max=50, message="Namen müssen 2-50 Buchstaben lang sein.")
    private String name;

   @NotNull
    @Size(min=2, max=50, message="Beschreibung muss 2-50 Buchstaben lang sein.")
    private String description;

    @NotNull
    @Range(min = 1L,message="Preis muss einer positiven Zahl größer 1 entsprechen.")
    private int cost;

    @NotNull
    @Range(min = 1L,message="Kaution muss einer positiven Zahl größer 1 entsprechen.")
    private int deposit;

    @NotNull
    @Range(min = 1L,message="Verfügbarkeit muss einer positiven Zahl größer 1 entsprechen.")
    private int availableTime;

    @NotNull
    @Size(min=2, max=50, message="Abholort muss 2-50 Buchstaben lang sein.")
    private String location;
}


