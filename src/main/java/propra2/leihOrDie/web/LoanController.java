package propra2.leihOrDie.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

    // 0 error

    @PostMapping(value="/request/{itemId}", produces="application/json")
    @ResponseBody
    public MultiValueMap<String, String> requestLoan(Model model, @Valid LoanForm form, @CookieValue(value="SessionID", defaultValue="") String sessionId, @PathVariable Long itemId) {
        User user = sessionRepository.findUserBySessionCookie(sessionId);
        Item item = itemRepository.findById(itemId).get();

        if (!item.isAvailability()) {
           return createErrorDict("Gegenstand ist nicht verfügbar.");
        }

        if (form.getLoanDuration() > item.getAvailableTime()) {
            return createErrorDict("Maximale Ausleihdauer ist überschritten.");
        }

        if (item.getUser() == user) {
            return createErrorDict("Du kannst deinen eigenen Gegenstand nicht ausleihen");
        }

        Long proPayReservationId = null;

        Loan loan = new Loan("pending", form.getLoanDuration(), user, item, proPayReservationId);
        saveLoan(loan);

        item.setAvailability(false);
        itemRepository.save(item);

        return createSuccessDict();
    }

    private void saveLoan(Loan loan) {
        loanRepository.save(loan);
    }

    private MultiValueMap<String, String> createErrorDict(String errorMessage) {
        MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
        errorMap.add("Error", errorMessage);
        return errorMap;
    }

    private MultiValueMap<String, String> createSuccessDict() {
        MultiValueMap<String, String> succcessMap = new LinkedMultiValueMap<>();
        succcessMap.add("Success", "Eine Anfrage wurde gesendet.");
        return succcessMap;
    }
}
