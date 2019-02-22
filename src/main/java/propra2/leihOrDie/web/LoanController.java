package propra2.leihOrDie.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PostMapping;
import propra2.leihOrDie.dataaccess.ItemRepository;
import propra2.leihOrDie.dataaccess.LoanRepository;
import propra2.leihOrDie.dataaccess.SessionRepository;
import propra2.leihOrDie.dataaccess.UserRepository;
import propra2.leihOrDie.model.Item;
import propra2.leihOrDie.model.Loan;
import propra2.leihOrDie.model.User;

import javax.validation.Valid;

import static propra2.leihOrDie.web.ProPayWrapper.raiseBalanceOfUser;
import static propra2.leihOrDie.web.ProPayWrapper.reserve;

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

    @PostMapping("/request/{itemId}")
    public String requestLoan(Model model, @Valid LoanForm form, @CookieValue(value="SessionID", defaultValue="") String sessionId, @PathVariable Long itemId) {
        User user = sessionRepository.findUserBySessionCookie(sessionId);
        Item item = itemRepository.findById(itemId).get();

        if (!item.isAvailability() && form.getLoanDuration() > item.getAvailableTime()) {
            return "redirect:/borrowall/" + itemId.toString();
        }

        Long propayReservationId;
        try {
            raiseBalanceOfUser(user.getEmail(), 10000);
            propayReservationId = reserve(user.getEmail(), item.getUser().getEmail(), item.getDeposit()).getId();
        } catch (Exception e) {
            return "redirect:/sourceAndTargetMustBeDifferent/";
        }

        Loan loan = new Loan("pending", form.getLoanDuration(), user, item, propayReservationId);
        saveLoan(loan);

        item.setAvailability(false);

        return "redirect:/request/success";
    }

    private void saveLoan(Loan loan) {
        loanRepository.save(loan);
    }

    private String createErrorMap(String errorMessage) {
        MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
        errorMap.add("Error", errorMessage);
        return errorMap.toString();
    }
}
