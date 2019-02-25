package propra2.leihOrDie.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import propra2.leihOrDie.dataaccess.ItemRepository;
import propra2.leihOrDie.dataaccess.LoanRepository;
import propra2.leihOrDie.dataaccess.SessionRepository;
import propra2.leihOrDie.dataaccess.UserRepository;
import propra2.leihOrDie.model.Item;
import propra2.leihOrDie.model.Loan;
import propra2.leihOrDie.model.User;

import java.util.ArrayList;
import java.util.List;

import static propra2.leihOrDie.web.ProPayWrapper.getBalanceOfUser;

@Controller
public class UserController {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/{username}")
    public String showUser(Model model, @PathVariable String username, @CookieValue(value="SessionID", defaultValue="") String sessionId) {
        if (!isAuthorized(sessionId, username)) {
            return "";
        }
        model.addAttribute("user", username);
        model.addAttribute("pendingitems", getPendingItems(username));
        model.addAttribute("loans", loanRepository.findLoansOfUser(username));
        model.addAttribute("items", itemRepository.findItemsOfUser(username));
        return "user";
    }

    @GetMapping("/user/propay/{username}")
    public String showPropay(Model model, @PathVariable String username, @CookieValue(value="SessionID", defaultValue="") String sessionId) {
        if (!isAuthorized(sessionId, username)) {
            return "";
        }
        User user = userRepository.findById(username).get();
        double kontostand = getBalanceOfUser(user.getEmail());

        model.addAttribute(kontostand);
        return "";
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

    private List<Loan> getPendingItems(String username) {
        List<Item> itemsOfUser = itemRepository.findItemsOfUser(username);
        List<Loan> loans = new ArrayList<>();
        for (Item i: itemsOfUser) {
            Loan temp = null;
            temp = loanRepository.findLoansOfItem(i.getId()).get(0);
            if (temp.getState().equals("pending")) {
                loans.add(temp);
            }
        }
        return loans;
    }

    private boolean isAuthorized(String sessionId, String username) {
        User user = sessionRepository.findUserBySessionCookie(sessionId);
        if (user.getUsername().equals(username)) {
            return true;
        }
        return false;
    }
}