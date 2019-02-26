package propra2.leihOrDie.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import propra2.leihOrDie.dataaccess.*;
import propra2.leihOrDie.model.Item;
import propra2.leihOrDie.model.Loan;
import propra2.leihOrDie.model.Transaction;
import propra2.leihOrDie.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static propra2.leihOrDie.web.ProPayWrapper.getBalanceOfUser;
import static propra2.leihOrDie.web.ProPayWrapper.raiseBalanceOfUser;

@Controller
public class UserController {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/myaccount")
    public String showUserPage(Model model, @CookieValue(value="SessionID", defaultValue="") String sessionId) {
        User user = sessionRepository.findUserBySessionCookie(sessionId);
        String username = user.getUsername();

        if (!isAuthorized(user, username)) {
            return "";
        }

        model.addAttribute("user", username);
        model.addAttribute("pendingitems", getPendingItems(username));
        model.addAttribute("loans", loanRepository.findLoansOfUser(username));
        model.addAttribute("items", itemRepository.findItemsOfUser(username));

        return "user";
    }

    @GetMapping("/user/{username}")
    public String showUser(Model model, @PathVariable String username, @CookieValue(value="SessionID", defaultValue="") String sessionId) {
        model.addAttribute("user", username);
        model.addAttribute("items", itemRepository.findItemsOfUser(username));
        return "other-user";
    }

    @GetMapping("/myaccount/propay")
    public String showPropay(Model model, @CookieValue(value="SessionID", defaultValue="") String sessionId, TransactionForm form) {
        User user = sessionRepository.findUserBySessionCookie(sessionId);
        double bankBalance = getBalanceOfUser(user.getEmail());

        List<Transaction> transactions = transactionRepository.findAllTransactionsOfUser(user.getUsername());

        model.addAttribute("bankBalance", bankBalance);
        model.addAttribute("transactions", transactions);


        return "user-propay";
    }

    @PostMapping("/myaccount/propay")
    public String doTransaction(Model model, @Valid TransactionForm form, BindingResult bindingResult, @CookieValue(value="SessionID", defaultValue="") String sessionId) {
        if(bindingResult.hasErrors()) {
            User user = sessionRepository.findUserBySessionCookie(sessionId);
            double bankBalance = getBalanceOfUser(user.getEmail());
            model.addAttribute("bankBalance", bankBalance);
            return "user-propay";
        }

        User user = sessionRepository.findUserBySessionCookie(sessionId);
        Transaction transaction = new Transaction(user, user, form.getAmount(), "Überweisung");
        transactionRepository.save(transaction);

        raiseBalanceOfUser(user.getEmail(), form.getAmount());
        return "redirect:/myaccount/propay";
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

    private boolean isAuthorized(User user, String username) {
        return user.getUsername().equals(username);
    }
}