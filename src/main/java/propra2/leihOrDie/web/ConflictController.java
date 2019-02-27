package propra2.leihOrDie.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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

import java.util.List;

import static propra2.leihOrDie.web.ProPayWrapper.*;

@Controller
public class ConflictController {
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


    @PostMapping("/conflict/open/{loanId}")
    public ResponseEntity openConflict(Model model, @PathVariable Long loanId,
                                       @CookieValue(value="SessionID", defaultValue="") String sessionId) {
        if (!loanRepository.findById(loanId).isPresent()) {
            return responseBuilder.createBadRequestResponse("Die angefragte Ausleihe existiert nicht.");
        }

        Loan loan = loanRepository.findById(loanId).get();

        if (authorizationHandler.isAuthorized(sessionId, loan.getItem()) ||
                authorizationHandler.isAuthorized(sessionId, loan.getUser())) {
            return responseBuilder.createUnauthorizedResponse();
        }

        if (!loan.getState().equals("active")) {
            return responseBuilder.createBadRequestResponse("Status der Ausleihe ist nicht \"aktiv\".");
        }

        loan.setState("conflict");
        loanRepository.save(loan);

        loan.getItem().setAvailability(false);
        itemRepository.save(loan.getItem());

        String message = "Konflikt wurde erstellt. Bitte sende eine Email mit der genauen Beschreibung Deines Problems " +
                "und dem Betreff \"" + sessionRepository.findUserBySessionCookie(sessionId).getUsername() + " - " +
                loan.getId().toString() + "\" an conflict@leihordie.de";

        return responseBuilder.createSuccessResponse(message);
    }

    @PostMapping("/conflict/solve/{loanId}")
    public ResponseEntity solveConflict(Model model, ConflictForm form, @PathVariable Long loanId,
                                        @CookieValue(value="SessionID", defaultValue="") String sessionId) {
        if (!authorizationHandler.isAdmin(sessionId)) {
            return responseBuilder.createUnauthorizedResponse();
        }

        if (!loanRepository.findById(loanId).isPresent()) {
            return responseBuilder.createBadRequestResponse("Loan " + loanId + " exisitert nicht.");
        }

        Loan loan = loanRepository.findById(loanId).get();

        String covenanteeEmail = form.getCovenanteeEmail();

        if (userRepository.findUserByEMail(covenanteeEmail).isEmpty()) {
            return responseBuilder.createBadRequestResponse("User " + covenanteeEmail + " existiert nicht.");
        }


        User convenantee = userRepository.findUserByEMail(covenanteeEmail).get(0);
        User lendingUser = userRepository.findUserByEMail(convenantee.getEmail()).get(0);

        if (convenantee.getEmail().equals(loan.getUser().getEmail())) {
            try {
                freeReservationOfUser(lendingUser.getEmail(), loan.getProPayReservationId());
            } catch (Exception e) {
                return responseBuilder.createBadRequestResponse("ProPay Fehler");
            }

        } else if(convenantee.getEmail().equals(loan.getItem().getUser().getEmail())) {
            try {
                punishAccount(lendingUser.getEmail(), loan.getProPayReservationId());
            } catch (Exception e) {
                return responseBuilder.createBadRequestResponse("ProPay Fehler");
            }

        } else {
            return responseBuilder.createBadRequestResponse("User " + covenanteeEmail + " steht nicht im Kontext mit Loan " +
                    loan.getId() + ".");
        }

        loan.setState("completed");
        loanRepository.save(loan);

        Item item = loan.getItem();
        item.setAvailability(false);
        itemRepository.save(item);

        return responseBuilder.createSuccessResponse("Konflikt wurde gelöst.");
    }

    @PostMapping("/conflict/solveerror/{loanId}")
    public ResponseEntity solveError(Model model, @PathVariable Long loanId,
                                     @CookieValue(value="SessionID", defaultValue="") String sessionId) {
        if (!authorizationHandler.isAdmin(sessionId)) {
            return responseBuilder.createUnauthorizedResponse();
        }

        Loan loan = loanRepository.findById(loanId).get();

        if (!loan.getState().equals("error")) {
            return responseBuilder.createBadRequestResponse("Es liegt kein Fehler bei dieser Ausleihe vor.");
        }

        int amount = loan.getItem().getCost() * loan.getDuration();
        String lenderEmail = loan.getUser().getEmail();
        String ownerEmail = loan.getItem().getUser().getEmail();

        try {
            transferMoney(lenderEmail, ownerEmail, amount);
        } catch (Exception e) {
            return responseBuilder.createProPayErrorResponse();
        }

        loan.setState("completed");
        loanRepository.save(loan);

        return responseBuilder.createSuccessResponse("Überweisung wurde getätigt.");
    }

    @GetMapping("/conflict/admin")
    public String getOpenConflicts(Model model, @CookieValue(value="SessionID", defaultValue="") String sessionId) {
        if (!authorizationHandler.isAdmin(sessionId)) {
            return "redirect:/";
        }

        List<Loan> openConflicts = loanRepository.findLoansByState("conflict");
        model.addAttribute("conflicts", openConflicts);

        List<Loan> openErrors = loanRepository.findLoansByState("error");
        model.addAttribute("errors", openErrors);

        return "conflict-list";
    }
}
