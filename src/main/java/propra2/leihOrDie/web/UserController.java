package propra2.leihOrDie.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

import javax.servlet.http.HttpServletResponse;
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

    private ResponseBuilder responseBuilder = new ResponseBuilder();

    @GetMapping("/myaccount")
    public String showUserPage(Model model, @CookieValue(value="SessionID", defaultValue="") String sessionId,
                               HttpServletResponse response) {
        User user = sessionRepository.findUserBySessionCookie(sessionId);
        String userName = user.getUsername();

        model.addAttribute("user", userName);
        model.addAttribute("pendingloans", getPendingLoans(userName));
        model.addAttribute("acceptedloans", getAcceptedLoans(userName));
        model.addAttribute("activeloans", getActiveLoans(userName));
        model.addAttribute("loans", loanRepository.findLoansOfUser(userName));
        model.addAttribute("items", itemRepository.findItemsOfUser(userName));

        return "user";
    }

    @GetMapping("/user/{username}")
    public String showUser(Model model, @PathVariable String username, @CookieValue(value="SessionID", defaultValue="") String sessionId) {
        model.addAttribute("user", username);
        model.addAttribute("items", itemRepository.findItemsOfUser(username));
        return "other-user";
    }

    @GetMapping("/myaccount/propay")
    public String showPropay(Model model, @CookieValue(value="SessionID", defaultValue="") String sessionId, HttpServletResponse response, TransactionForm form) {
        User user = sessionRepository.findUserBySessionCookie(sessionId);

        double bankBalance = getBalanceOfUser(user.getEmail());

        List<Transaction> transactions = transactionRepository.findAllTransactionsOfUser(user.getUsername());

        model.addAttribute("bankBalance", bankBalance);
        model.addAttribute("transactions", transactions);

        return "user-propay";
    }

    @PostMapping("/myaccount/propay")
    public ResponseEntity doTransaction(Model model, @Valid TransactionForm form, BindingResult bindingResult, @CookieValue(value="SessionID", defaultValue="") String sessionId) {
        if(bindingResult.hasErrors()) {
            return responseBuilder.createBadRequestResponse("Der Mindestbetrag beläuft sich auf einen Euro!");
        }

        User user = sessionRepository.findUserBySessionCookie(sessionId);

        try {
            raiseBalanceOfUser(user.getEmail(), form.getAmount());

            Transaction transaction = new Transaction(user, user, form.getAmount(), "Überweisung");
            transactionRepository.save(transaction);
        } catch (Exception e) {
            return responseBuilder.createBadRequestResponse("Es war nicht möglich den Betrag zu Überweisen. Bitte versuchen sie es später nochmal.");
        }

        return responseBuilder.createSuccessResponse("Überweisung erfolgreich!");
    }

    //neu
    @GetMapping("/reloaditems")
    public String reloadItems(Model model, @CookieValue(value="SessionID", defaultValue="") String sessionId,
                              HttpServletResponse response) {
        User user = sessionRepository.findUserBySessionCookie(sessionId);
        String userName = user.getUsername();

        model.addAttribute("pendingloans", getPendingLoans(userName));
        model.addAttribute("acceptedloans", getAcceptedLoans(userName));
        model.addAttribute("activeloans", getActiveLoans(userName));
        model.addAttribute("loans", loanRepository.findLoansOfUser(userName));
        model.addAttribute("items", itemRepository.findItemsOfUser(userName));

        return "loan-snippet";
    }

    private List<Loan> getPendingLoans(String userName) {
        List<Item> itemsOfUser = itemRepository.findItemsOfUser(userName);
        List<Loan> loans = new ArrayList<>();

        for (Item item: itemsOfUser) {
            Loan loan = loanRepository.findLoansOfItem(item.getId()).get(0);

            if (loan.getState().equals("pending")) {
                loans.add(loan);
            }
        }

        return loans;
    }

    private List<Loan> getAcceptedLoans(String userName) {
        List<Item> itemsOfUser = itemRepository.findItemsOfUser(userName);
        List<Loan> loans = new ArrayList<>();

        for (Item item: itemsOfUser) {
            Loan loan = loanRepository.findLoansOfItem(item.getId()).get(0);

            if (loan.getState().equals("accepted")) {
                loans.add(loan);
            }
        }

        return loans;
    }

    private List<Loan> getActiveLoans(String userName) {
        List<Item> itemsOfUser = itemRepository.findItemsOfUser(userName);
        List<Loan> loans = new ArrayList<>();

        for (Item item: itemsOfUser) {
            Loan loan = loanRepository.findLoansOfItem(item.getId()).get(0);

            if (loan.getState().equals("active")) {
                loans.add(loan);
            }
        }

        return loans;
    }

    private boolean isAuthorized(User user, String username) {
        return user.getUsername().equals(username);
    }
}