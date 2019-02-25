package propra2.leihOrDie.web;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
class ConflictForm {
    @NotNull
    @Email
    private String covenanteeEmail;
}
