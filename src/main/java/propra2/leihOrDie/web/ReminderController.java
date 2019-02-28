package propra2.leihOrDie.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import propra2.leihOrDie.dataaccess.LoanRepository;
import propra2.leihOrDie.dataaccess.SessionRepository;
import propra2.leihOrDie.dataaccess.UserRepository;
import propra2.leihOrDie.model.Loan;
import propra2.leihOrDie.model.User;
import propra2.leihOrDie.response.ResponseBuilder;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ReminderController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    SessionRepository sessionRepository;

    ResponseBuilder responseBuilder;

    @GetMapping("/reminde")
    public ResponseEntity getReminded(Model model, @CookieValue(value="SessionID", defaultValue="") String sessionId) {
        User user = sessionRepository.findUserBySessionCookie(sessionId);
        List<Loan> exceededLoansOfUser = getExceededLoans(user);

        model.addAttribute(user.getUsername());

        for (Loan loan: exceededLoansOfUser) {
            int exceededDays = numberOfExceededDays(loan);
            model.addAttribute(loan.getItem());
            model.addAttribute(exceededDays);
        }

        return responseBuilder.createProPayErrorResponse();
    }

    private List<Loan> getExceededLoans(User user) {
        List<Loan> loans = loanRepository.findLoansOfUser(user.getUsername());
        List<Loan> returnLoans = new ArrayList<>();

        for (Loan loan: loans) {
            if (loan.isExceeded()) {
                returnLoans.add(loan);
            }
        }

        return returnLoans;
    }

    private int numberOfExceededDays(Loan loan) {
        LocalDate startDate = loan.getDayOfRental();
        LocalDate now = LocalDate.now();

        Period differenceBetweenDays = Period.between(startDate, now);

        return differenceBetweenDays.getDays();
    }
}
