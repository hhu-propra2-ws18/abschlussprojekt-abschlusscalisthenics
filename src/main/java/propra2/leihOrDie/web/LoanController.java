package propra2.leihOrDie.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import propra2.leihOrDie.dataaccess.ItemRepository;
import propra2.leihOrDie.dataaccess.LoanRepository;
import propra2.leihOrDie.dataaccess.SessionRepository;
import propra2.leihOrDie.dataaccess.UserRepository;
import propra2.leihOrDie.model.Item;
import propra2.leihOrDie.model.Loan;
import propra2.leihOrDie.model.User;

import javax.validation.Valid;

import static propra2.leihOrDie.web.ProPayWrapper.*;

@Controller
public class LoanController {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    SessionRepository sessionRepository;

    @PostMapping(value="/request/{itemId}")
    @ResponseBody
    public ResponseEntity requestLoan(Model model, @Valid LoanForm form, @CookieValue(value="SessionID", defaultValue="") String sessionId, @PathVariable Long itemId) {
        User user = sessionRepository.findUserBySessionCookie(sessionId);
        Item item = itemRepository.findById(itemId).get();

        if (form.getLoanDuration() == 0) {
            return createErrorResponse("Die minimale Ausleihdauer beträgt einen Tag.");
        }

        if (!item.isAvailability()) {
           return createErrorResponse("Dieser Gegenstand ist nicht verfügbar.");
        }

        if (form.getLoanDuration() > item.getAvailableTime()) {
            return createErrorResponse("Die maximale Ausleihdauer ist überschritten.");
        }

        if (item.getUser() == user) {
            return createErrorResponse("Du kannst deinen eigenen Gegenstand nicht ausleihen.");
        }

        Long proPayReservationId;
        try {
            proPayReservationId = reserve(user.getEmail(), item.getUser().getEmail(), item.getDeposit()).getId();
        } catch (Exception e) {
            return createErrorResponse("ProPay Fehler");
        }

        Loan loan = new Loan("pending", form.getLoanDuration(), user, item, proPayReservationId);
        saveLoan(loan);

        item.setAvailability(false);
        itemRepository.save(item);

        return createSuccessResponse("Eine Anfrage wurde gesendet.");
    }

    @PostMapping("/request/accept/{loanId}")
    public ResponseEntity changeStatusToAccepted(Model model, @PathVariable Long loanId, @CookieValue(value="SessionID", defaultValue="") String sessionId) {
        Loan loan = loanRepository.findById(loanId).get();

        if (!isAuthorized(sessionId, loan.getItem().getId())) {
            return createErrorResponse("Du bist nicht authorisiert diese Aktion durchzuführen");
        }

        loan.setState("accepted");
        loanRepository.save(loan);

        return createSuccessResponse("Bestätigt.");
    }

    @PostMapping("/request/decline/{loanId}")
    public ResponseEntity changeStatusToDeclined(Model model, @PathVariable Long loanId, @CookieValue(value="SessionID", defaultValue="") String sessionId) {
        Loan loan = loanRepository.findById(loanId).get();
        Item item = itemRepository.findById(loan.getItem().getId()).get();

        if (!isAuthorized(sessionId, loan.getItem().getId())) {
            return createErrorResponse("Du bist nicht authorisiert diese Aktion durchzuführen");
        }

        loan.setState("declined");
        loanRepository.save(loan);

        item.setAvailability(true);
        itemRepository.save(item);

        return createSuccessResponse("Bestätigt.");
    }

    private void saveLoan(Loan loan) {
        loanRepository.save(loan);
    }

    private ResponseEntity createErrorResponse(String errorMessage) {
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity createSuccessResponse(String successMessage) {
        return new ResponseEntity<>(successMessage, HttpStatus.OK);
    }

    private boolean isAuthorized(String sessionId, Long itemId) {
        User user = sessionRepository.findUserBySessionCookie(sessionId);
        Item item = itemRepository.findById(itemId).get();
        
        return user.getUsername().equals(item.getUser().getUsername());
    }
}
