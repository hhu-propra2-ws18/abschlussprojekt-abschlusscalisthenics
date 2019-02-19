package propra2.leihOrDie.web;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import propra2.leihOrDie.UserDto;

public class SignupController {

    @RequestMapping(value="/user/registration", method=RequestMethod.GET)
    public String showRegistrationForm(WebRequest request, Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);

        return "registration";
    }


}
