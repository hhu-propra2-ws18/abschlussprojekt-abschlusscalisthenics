package propra2.leihOrDie.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import propra2.leihOrDie.dataaccess.ItemRepository;
import propra2.leihOrDie.dataaccess.LoanRepository;
import propra2.leihOrDie.dataaccess.PictureRepository;
import propra2.leihOrDie.dataaccess.UserRepository;
import propra2.leihOrDie.model.Item;
import propra2.leihOrDie.model.Loan;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoanRepository loanRepository;

    @GetMapping("/user/{username}")
    public String showUser(Model model, @PathVariable String username) {
        model.addAttribute(1);
        return "user";
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

    private List<Loan> getPendingItems(String username) {
        List<Item> itemsOfUser = itemRepository.findItemsOfUser(username);
        List<Loan> loans = new ArrayList<>();
        for (Item i: itemsOfUser) {
            loans.add(loanRepository.findPendingLoans(i.getId()).get(0));
        }
        return loans;
    }
}