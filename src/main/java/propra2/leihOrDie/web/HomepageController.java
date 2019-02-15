package propra2.leihOrDie.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomepageController {

    @GetMapping("")
    public String homepage(){
        return "";
    }

    @PostMapping("")
    public String home(){
        return "";
    }

}
