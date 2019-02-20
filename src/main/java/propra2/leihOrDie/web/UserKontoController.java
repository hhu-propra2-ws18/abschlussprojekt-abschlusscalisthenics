package propra2.leihOrDie.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import propra2.leihOrDie.dataaccess.ItemRepository;
import propra2.leihOrDie.dataaccess.LoanRepository;
import propra2.leihOrDie.dataaccess.PictureRepository;
import propra2.leihOrDie.dataaccess.UserRepository;

@Controller
public class UserKontoController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private UserRepository userRepository;

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

    private void loadArtikelTabelle(Model model){
      //  model.addAttribute();

    }
}
