package propra2.leihOrDie.web;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginForm {
    @NotNull
    @Size(min = 1, message = "Dieses Feld darf nicht leer bleiben")
    @Email(message = "Gib eine gültige Email-Adresse ein")
    private String email;

    @NotNull
    @Size(min = 6, max = 50, message="Bitte gib ein (sicheres) Passwort zwischen 6-50 Zeichen ein.")
    private String password;
}
