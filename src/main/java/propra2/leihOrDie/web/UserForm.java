package propra2.leihOrDie.web;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserForm {
    @NotNull
    @Size(min = 5, max = 50, message="Benutzernamen müssen 1-50 Zeichen lang sein")
    private String username;

    @NotNull
    @Size(min = 1, message = "Dieses Feld darf nicht leer bleiben")
    @Email(message = "Gib eine gültige Email-Adresse ein")
    private String email;

    @NotNull
    @Size(min = 6, max = 50, message="Bitte gib ein (sicheres) Passwort zwischen 6-50 Zeichen ein.")
    private byte[] password;
}
