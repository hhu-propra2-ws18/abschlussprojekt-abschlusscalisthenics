package propra2.leihOrDie.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import propra2.leihOrDie.model.Address;
import propra2.leihOrDie.model.User;
import propra2.leihOrDie.dataaccess.UserRepository;

import javax.validation.Valid;

@Controller
public class AuthenticationController {

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

        Address address = new Address();
        address.setStreet(form.getStreet());
        address.setHouseNumber(Integer.parseInt(form.getHouseNumber()));
        address.setPostcode(Integer.parseInt(form.getPostcode()));
        address.setCity(form.getCity());

        User user = new User();

        saveUser(user, form.getUsername(), form.getEmail(), form.getPassword(), address);

        return "redirect:/";
    }

    private void saveUser(User user, String name, String email, String password, Address address) {
        user.setUsername(name);
        user.setPassword(password);
        user.setEmail(email);
        user.setAddress(address);
        userRepository.save(user);
    }

    @GetMapping("/login")
    public String login(Model model, @Valid UserForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        return "/";
    }

    public boolean authentificateUser(String usermail, String password) {
        try {
            User user = userRepository.findUserByEMail(usermail).get(0);
            return user.verifyPassword(password);
        } catch (Exception e){
            return false;
        }
    }

}
