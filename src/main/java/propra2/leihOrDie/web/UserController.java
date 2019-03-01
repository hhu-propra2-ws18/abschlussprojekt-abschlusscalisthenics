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
import propra2.leihOrDie.form.TransactionForm;
import propra2.leihOrDie.model.Item;
import propra2.leihOrDie.model.Loan;
import propra2.leihOrDie.model.Transaction;
import propra2.leihOrDie.model.User;
import propra2.leihOrDie.propay.ProPayWrapper;
import propra2.leihOrDie.response.ResponseBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    private ProPayWrapper proPayWrapper = new ProPayWrapper();

    @GetMapping("/myaccount")
    public String showUserPage(Model model, @CookieValue(value="SessionID", defaultValue="") String sessionId,
                               HttpServletResponse response) {
        User user = sessionRepository.findUserBySessionCookie(sessionId);
        String userName = user.getUsername();

        model.addAttribute("user", userName);
        model.addAttribute("pendingloans", getLoansByStatus(userName, "pending"));
        model.addAttribute("acceptedloans", getLoansByStatus(userName, "accepted"));
        model.addAttribute("activeloans", getLoansByStatus(userName, "active"));
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

        double bankBalance = proPayWrapper.getBalanceOfUser(user.getEmail());

        List<Transaction> transactions = transactionRepository.findAllTransactionsOfUser(user.getUsername());
        List<String> formattedDates = getFormattedDate(transactions);
        Map <Long, String> map = new HashMap<Long, String>();
        for(int i = 0; i < transactions.size(); i++) {
            map.put(transactions.get(0).getId(), formattedDates.get(i));
        }

        model.addAttribute("bankBalance", bankBalance);
        model.addAttribute("transactions", transactions);
        model.addAttribute("map", map);

        return "user-propay";
    }

    @PostMapping("/myaccount/propay")
    public ResponseEntity doTransaction(Model model, @Valid TransactionForm form, BindingResult bindingResult, @CookieValue(value="SessionID", defaultValue="") String sessionId) {
        if(bindingResult.hasErrors()) {
            return responseBuilder.createBadRequestResponse("Der Mindestbetrag beläuft sich auf einen Euro!");
        }

        User user = sessionRepository.findUserBySessionCookie(sessionId);

        try {
            proPayWrapper.raiseBalanceOfUser(user.getEmail(), form.getAmount());

            Transaction transaction = new Transaction(user, user, form.getAmount(), "Überweisung");
            transactionRepository.save(transaction);
        } catch (Exception e) {
            return responseBuilder.createBadRequestResponse("Es war nicht möglich den Betrag zu Überweisen. Bitte versuchen sie es später nochmal.");
        }

        return responseBuilder.createSuccessResponse("Überweisung erfolgreich!");
    }

    @GetMapping("/reloaditems")
    public String reloadItems(Model model, @CookieValue(value="SessionID", defaultValue="") String sessionId,
                              HttpServletResponse response) {
        User user = sessionRepository.findUserBySessionCookie(sessionId);
        String userName = user.getUsername();

        model.addAttribute("pendingloans", getLoansByStatus(userName, "pending"));
        model.addAttribute("acceptedloans", getLoansByStatus(userName, "accepted"));
        model.addAttribute("activeloans", getLoansByStatus(userName, "active"));
        model.addAttribute("loans", loanRepository.findLoansOfUser(userName));
        model.addAttribute("items", itemRepository.findItemsOfUser(userName));

        return "loan-snippet";
    }

    private List<Loan> getLoansByStatus(String userName, String status) {
        List<Item> itemsOfUser = itemRepository.findItemsOfUser(userName);
        List<Loan> loans = new ArrayList<>();
        Loan loan;

        for (Item item: itemsOfUser) {
            List<Loan> loanList = loanRepository.findLoansOfItem(item.getId());

            if(loanList.size() != 0) {
                loan = loanList.get(0);
            } else {
                continue;
            }

            if (loan.getState().equals(status)) {
                loans.add(loan);
            }
        }

        return loans;
    }

    private List<String> getFormattedDate(List<Transaction> transactions) {
        List<String> dates = new ArrayList<>();
        for (Transaction transaction: transactions) {
            dates.add(transaction.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        }
        return dates;
    }
}
