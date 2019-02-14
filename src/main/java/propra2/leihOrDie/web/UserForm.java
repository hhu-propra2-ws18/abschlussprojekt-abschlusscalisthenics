package propra2.leihOrDie.web;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserForm {
    @NotNull
    @Size(min = 1, max = 100, message="Benutzernamen müssen 1-100 Buchstaben lang sein.")
    private String username;

    @NotNull
    @Email(message = "Bitte gebe eine gültige Email-Adresse ein.")
    private String email;

    @NotNull
    @Size(min = 6, max = 50, message="Bitte geben Sie ein (sicheres) Passwort zwischen 6-50 Buchstaben ein.")
    private byte[] password;
}
