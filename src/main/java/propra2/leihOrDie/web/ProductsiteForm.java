package propra2.leihOrDie.web;

import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Range;
import propra2.leihOrDie.model.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ProductsiteForm {

    @NotNull
    @Size(min = 1, max = 10, message = "Die Anzahl der Tage muss eine Zahl zwischen 1 und 10 sein")
    private String ausleihTage;
}