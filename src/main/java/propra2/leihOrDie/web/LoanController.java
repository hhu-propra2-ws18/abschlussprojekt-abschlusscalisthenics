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
    public ResponseEntity requestLoan(Model model, @Valid LoanForm form,
                                      @CookieValue(value="SessionID", defaultValue="") String sessionId,
                                      @PathVariable Long itemId) {
        User user = sessionRepository.findUserBySessionCookie(sessionId);
        Item item = itemRepository.findById(itemId).get();

        if (form.getLoanDuration() == 0) {
            return createBadRequestResponse("Die minimale Ausleihdauer beträgt einen Tag.");
        }

        if (!item.isAvailability()) {
           return createBadRequestResponse("Dieser Gegenstand ist nicht verfügbar.");
        }

        if (form.getLoanDuration() > item.getAvailableTime()) {
            return createBadRequestResponse("Die maximale Ausleihdauer ist überschritten.");
        }

        if (item.getUser() == user) {
            return createBadRequestResponse("Du kannst deinen eigenen Gegenstand nicht ausleihen.");
        }

        Long proPayReservationId;
        try {
            proPayReservationId = reserve(user.getEmail(), item.getUser().getEmail(), item.getDeposit()).getId();
        } catch (Exception e) {
            return createBadRequestResponse("ProPay Fehler");
        }

        Loan loan = new Loan("pending", form.getLoanDuration(), user, item, proPayReservationId);
        loanRepository.save(loan);

        item.setAvailability(false);
        itemRepository.save(item);

        return createSuccessResponse("Eine Anfrage wurde gesendet.");
    }

    @PostMapping("/request/accept/{loanId}")
    public ResponseEntity changeStatusToAccepted(Model model, @PathVariable Long loanId,
                                                 @CookieValue(value="SessionID", defaultValue="") String sessionId) {
        Loan loan = loanRepository.findById(loanId).get();

        if (!isAuthorized(sessionId, loan.getItem())) {
            return createUnauthorizedResponse();
        }

        loan.setState("accepted");
        loanRepository.save(loan);

        return createSuccessResponse("Bestätigt.");
    }

    @PostMapping("/request/decline/{loanId}")
    public ResponseEntity changeStatusToDeclined(Model model, @PathVariable Long loanId,
                                                 @CookieValue(value="SessionID", defaultValue="") String sessionId) {
        Loan loan = loanRepository.findById(loanId).get();
        Item item = itemRepository.findById(loan.getItem().getId()).get();

        if (!isAuthorized(sessionId, loan.getItem())) {
            return createUnauthorizedResponse();
        }

        loan.setState("declined");
        loanRepository.save(loan);

        item.setAvailability(true);
        itemRepository.save(item);

        return createSuccessResponse("Bestätigt.");
    }

    @PostMapping("/conflict/open/{loanId}")
    public ResponseEntity openConflict(Model model, @PathVariable Long loanId,
                                       @CookieValue(value="SessionID", defaultValue="") String sessionId) {
        if (!loanRepository.findById(loanId).isPresent()) {
            return createBadRequestResponse("Die angefragte Ausleihe existiert nicht.");
        }

        Loan loan = loanRepository.findById(loanId).get();

        if (!isAuthorized(sessionId, loan.getItem()) || !isAuthorized(sessionId, loan.getUser())) {
            return createUnauthorizedResponse();
        }

        if (!loan.getState().equals("active")) {
            return createBadRequestResponse("Status der Ausleihe ist nicht \"aktiv\".");
        }

        loan.setState("conflict");
        loanRepository.save(loan);

        loan.getItem().setAvailability(false);
        itemRepository.save(loan.getItem());

        String message = "Konflikt wurde erstellt. Bitte sende eine Email mit der genauen Beschreibung Deines Problems " +
                "und dem Betreff \"" + sessionRepository.findUserBySessionCookie(sessionId).getUsername() + " - " +
                loan.getId().toString() + "\" an conflict@leihordie.de";

        return createSuccessResponse(message);
    }

    @PostMapping("/conflict/solve/{loanId}")
    public ResponseEntity solveConflict(Model model, ConflictForm form, @PathVariable Long loanId,
                                        @CookieValue(value="SessionID", defaultValue="") String sessionId) {
        if (!isAdmin(sessionId)) {
            return createUnauthorizedResponse();
        }

        if (!loanRepository.findById(loanId).isPresent()) {
            return createBadRequestResponse("Loan " + loanId + " exisitert nicht.");
        }

        Loan loan = loanRepository.findById(loanId).get();

        String covenanteeEmail = form.getCovenanteeEmail();

        if (userRepository.findUserByEMail(covenanteeEmail).isEmpty()) {
            return createBadRequestResponse("User " + covenanteeEmail + " existiert nicht.");
        }


        User convenantee = userRepository.findUserByEMail(covenanteeEmail).get(0);
        User lendingUser = userRepository.findUserByEMail(convenantee.getEmail()).get(0);

        if (convenantee.getEmail().equals(loan.getUser().getEmail())) {
            try {
                freeReservationOfUser(lendingUser.getEmail(), loan.getProPayReservationId());
            } catch (Exception e) {
                return createBadRequestResponse("ProPay Fehler");
            }

        } else if(convenantee.getEmail().equals(loan.getItem().getUser().getEmail())) {
            try {
                punishAccount(lendingUser.getEmail(), loan.getProPayReservationId());
            } catch (Exception e) {
                return createBadRequestResponse("ProPay Fehler");
            }

        } else {
            return createBadRequestResponse("User " + covenanteeEmail + " steht nicht im Kontext mit Loan " +
                    loan.getId() + ".");
        }

        loan.setState("completed");
        loanRepository.save(loan);

        Item item = loan.getItem();
        item.setAvailability(false);
        itemRepository.save(item);

        return createSuccessResponse("Konflikt wurde gelöst.");
    }

    private ResponseEntity createBadRequestResponse(String errorMessage) {
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity createUnauthorizedResponse() {
        String errorMessage = "Du bist nicht authorisiert diese Aktion durchzuführen.";
        return  new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity createSuccessResponse(String successMessage) {
        return new ResponseEntity<>(successMessage, HttpStatus.OK);
    }

    private boolean isAuthorized(String sessionId, Item item) {
        User user = sessionRepository.findUserBySessionCookie(sessionId);

        return user.getUsername().equals(item.getUser().getUsername());
    }

    private boolean isAuthorized(String sessionId, User user) {
        User sessionUser = sessionRepository.findUserBySessionCookie(sessionId);

        return sessionUser.getUsername().equals(user.getUsername());
    }

    private boolean isAdmin(String sessionId) {
        return sessionRepository.findUserBySessionCookie(sessionId).getRole().equals("ADMIN");
    }
}
