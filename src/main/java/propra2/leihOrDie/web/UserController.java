package propra2.leihOrDie.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import propra2.leihOrDie.dataaccess.UserRepository;

@Controller
public class UserController {
    //@Autowired
    //private UserRepository userRepository;

    @GetMapping("/login")
    public String user() {
        return "login";
    }

    @PostMapping("/login")
    public String logUser() {
        return "login";
    }

    @GetMapping("/signup")
    public String see() {
        return "signup";
    }

    @PostMapping("/signup")
    public String newUser() {
        return "signup";
    }


}
