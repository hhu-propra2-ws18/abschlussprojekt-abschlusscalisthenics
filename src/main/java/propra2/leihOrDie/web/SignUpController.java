package propra2.leihOrDie.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import propra2.leihOrDie.model.User;
import propra2.leihOrDie.dataaccess.UserRepository;

import javax.validation.Valid;

@Controller
public class SignUpController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/registration")
    public String see(Model model, UserForm form) {
        return "registration";
    }

    @PostMapping("/user/registration")
    public String newUser(Model model, @Valid UserForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }

        User user = new User();

        saveUser(user, form.getUsername(), form.getEmail(), form.getPassword());

        return "redirect:/";
    }

    private void saveUser(User user, String name, String email, byte[] password) {
        user.setUsername(name);
        user.setPassword(password);
        user.setEmail(email);
        userRepository.save(user);
    }


}
