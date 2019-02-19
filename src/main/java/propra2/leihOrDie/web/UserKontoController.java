package propra2.leihOrDie.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserKontoController {

    @GetMapping("/Userkonto")
    public String Konto(){
        return "Userkonto";
    }

    @GetMapping("ArtikelEinstellen")
    public String Artikeleinstellen(){
        return "ArtikelEinstellen";
    }

    @GetMapping("Artikelliste")
    public String Artikelliste(){
        return "Artikelliste";
    }
}
