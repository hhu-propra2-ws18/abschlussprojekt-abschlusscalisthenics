package propra2.leihOrDie.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import propra2.leihOrDie.dataaccess.ItemRepository;
import propra2.leihOrDie.dataaccess.LoanRepository;
import propra2.leihOrDie.dataaccess.SessionRepository;
import propra2.leihOrDie.dataaccess.UserRepository;
import propra2.leihOrDie.model.Item;
import propra2.leihOrDie.model.Loan;
import propra2.leihOrDie.model.User;

import javax.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

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

    private ResponseBuilder responseBuilder = new ResponseBuilder();
    @Autowired
    private AuthorizationHandler authorizationHandler = new AuthorizationHandler(sessionRepository);


    @PostMapping(value="/request/{itemId}")
    @ResponseBody
    public ResponseEntity requestLoan(Model model, @Valid LoanForm form,
                                      @CookieValue(value="SessionID", defaultValue="") String sessionId,
                                      @PathVariable Long itemId) {
        User user = sessionRepository.findUserBySessionCookie(sessionId);
        Item item = itemRepository.findById(itemId).get();

        if (form.getLoanDuration() == 0) {
            return responseBuilder.createBadRequestResponse("Die minimale Ausleihdauer beträgt einen Tag.");
        }

        if (!item.isAvailability()) {
           return responseBuilder.createBadRequestResponse("Dieser Gegenstand ist nicht verfügbar.");
        }

        if (form.getLoanDuration() > item.getAvailableTime()) {
            return responseBuilder.createBadRequestResponse("Die maximale Ausleihdauer ist überschritten.");
        }

        if (item.getUser() == user) {
            return responseBuilder.createBadRequestResponse("Du kannst deinen eigenen Gegenstand nicht ausleihen.");
        }

        Long proPayReservationId;
        try {
            proPayReservationId = reserve(user.getEmail(), item.getUser().getEmail(), item.getDeposit()).getId();
        } catch (Exception e) {
            return responseBuilder.createBadRequestResponse("ProPay Fehler");
        }

        Loan loan = new Loan("pending", form.getLoanDuration(), user, item, proPayReservationId);
        loanRepository.save(loan);

        item.setAvailability(false);
        itemRepository.save(item);
        return responseBuilder.createSuccessResponse("Eine Anfrage wurde gesendet.");
    }

    @PostMapping("/request/accept/{loanId}")
    public ResponseEntity changeStatusToAccepted(Model model, @PathVariable Long loanId,
                                                 @CookieValue(value="SessionID", defaultValue="") String sessionId) {
        Loan loan = loanRepository.findById(loanId).get();

        if (!authorizationHandler.isAuthorized(sessionId, loan.getItem())) {
            return responseBuilder.createUnauthorizedResponse();
        }

        loan.setState("accepted");
        loanRepository.save(loan);

        return responseBuilder.createSuccessResponse("Bestätigt.");
    }

    @PostMapping("/request/decline/{loanId}")
    public ResponseEntity changeStatusToDeclined(Model model, @PathVariable Long loanId,
                                                 @CookieValue(value="SessionID", defaultValue="") String sessionId) {
        Loan loan = loanRepository.findById(loanId).get();
        Item item = itemRepository.findById(loan.getItem().getId()).get();

        if (!authorizationHandler.isAuthorized(sessionId, loan.getItem())) {
            return responseBuilder.createUnauthorizedResponse();
        }

        loan.setState("declined");
        loanRepository.save(loan);

        item.setAvailability(true);
        itemRepository.save(item);

        return responseBuilder.createSuccessResponse("Bestätigt.");
    }

    @PostMapping("/request/activate/{loanId}")
    public ResponseEntity changeStatusToActive(Model model, @PathVariable Long loanId, @CookieValue(value="SessionID", defaultValue="") String sessionId) {
        Loan loan = loanRepository.findById(loanId).get();

        if (!authorizationHandler.isAuthorized(sessionId, loan.getItem().getUser())) {
            return responseBuilder.createUnauthorizedResponse();
        }

        loan.setState("active");
        loan.setStartDate(LocalDateTime.now());
        loanRepository.save(loan);

        return responseBuilder.createSuccessResponse("Bestätigt");
    }

    @PostMapping("/request/return/{loanId}")
    public ResponseEntity changeStatusToCompleted(Model model, @PathVariable Long loanId, @CookieValue(value="SessionID", defaultValue="") String sessionId) {
        User user = sessionRepository.findUserBySessionCookie(sessionId);
        Loan loan = loanRepository.findById(loanId).get();

        if (!authorizationHandler.isAuthorized(sessionId, loan.getItem().getUser())) {
            return responseBuilder.createUnauthorizedResponse();
        }

        double amount = loan.getDuration() * loan.getItem().getCost();

        loan.setEndDate(LocalDateTime.now());

        try {
            transferMoney(loan.getUser().getEmail(), user.getEmail(), amount);
        } catch (Exception e) {
            loan.setState("error");
            loanRepository.save(loan);
            return responseBuilder.createBadRequestResponse("Es war nicht möglich den Betrag zu überweisen. Bitte sende eine Email mit der genauen Beschreibung Deines Problems und dem Betreff \"" + sessionRepository.findUserBySessionCookie(sessionId).getUsername() + " - " + loan.getId().toString() + "\" an conflict@leihordie.de");
        }

        loan.setState("completed");
        loanRepository.save(loan);

        return responseBuilder.createSuccessResponse("Erfolgreich!");
    }
}
