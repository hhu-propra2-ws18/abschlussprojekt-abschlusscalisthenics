package propra2.leihOrDie.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import propra2.leihOrDie.dataaccess.UserRepository;
import propra2.leihOrDie.model.User;

import javax.validation.Valid;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String user(Model model, UserForm form) {
        return "login";
    }

    @PostMapping("/login")
    public String logUser(Model model, @Valid UserForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        // proceed with right data...
        return "redirect:/";
    }

    @GetMapping("/signup")
    public String see(Model model, UserForm form) {
        return "signup";
    }

    @PostMapping("/signup")
    public String newUser(Model model, @Valid UserForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup";
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

