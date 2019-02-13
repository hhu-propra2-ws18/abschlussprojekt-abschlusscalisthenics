package propra2.leihOrDie.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import propra2.leihOrDie.dataaccess.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {
    //@Autowired
    //private UserRepository userRepository;

    @GetMapping("/")
    public String new_user() {
        return "login";
    }
}
