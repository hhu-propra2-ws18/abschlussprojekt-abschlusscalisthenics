package propra2.leihOrDie.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import propra2.leihOrDie.dataaccess.ItemRepository;
import propra2.leihOrDie.dataaccess.LoanRepository;
import propra2.leihOrDie.dataaccess.UserRepository;
import propra2.leihOrDie.model.Item;
import propra2.leihOrDie.model.Loan;

import javax.validation.Valid;

import static propra2.leihOrDie.web.ProPayWrapper.reserve;

public class LoanController {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LoanRepository loanRepository;

    @PostMapping("/request/{id}")
    public String requestLoan(Model model, @Valid LoanForm form, @CookieValue(value="SessionID", defaultValue="") String sessionId, @PathVariable Long itemId) {

        //User user = userRepository.findUserByEMail(form.getMail());
        Item item = itemRepository.findById(itemId).get(0);

        if (!item.isAvailability()) {
            return "";
        }

        try {
            Long propayReservationId = reserve(user.getEmail(), item.getUser().getEmail(), item.getDeposit()).getId();
        } catch (Exception e) {
            return "";
        }

        Loan loan = new Loan("pending", form.getDuration(), user.getEmail(), itemId, propayReservationId);
        saveLoan(loan);

        item.setAvailability(false);

        return "";
    }

    private void saveLoan(Loan loan) {
        loanRepository.save(loan);
    }
}
