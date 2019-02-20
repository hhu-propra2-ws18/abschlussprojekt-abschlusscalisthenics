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
import propra2.leihOrDie.model.Item;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserKontoController {

    @Autowired
    private ItemRepository itemRepository;

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

    private List<Item> collectArtikel(Long[] itemID){
        List<Item> items = new ArrayList<>();
        if (itemID == null)
            return items;
        for (int i=0; i<itemID.length; i++){
    //        items.add(ItemRepository.findById(itemID[i].get());
        }
        return items;
    }

    private void loadAusgelieheneArtikel(Model model, Item item){
        model.addAttribute("id", item.getId());
        model.addAttribute("name", item.getName());
    }
    private void loadArtikelAnfragen(Model model, Item item){
        model.addAttribute("id", item.getId());
        model.addAttribute("Name", item.getName());
    }
}