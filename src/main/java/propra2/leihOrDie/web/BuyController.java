package propra2.leihOrDie.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import propra2.leihOrDie.dataaccess.*;
import propra2.leihOrDie.model.Buy;
import propra2.leihOrDie.model.Item;
import propra2.leihOrDie.model.Transaction;
import propra2.leihOrDie.model.User;

import java.util.List;

import static propra2.leihOrDie.web.ProPayWrapper.transferMoney;

@Controller
@ResponseBody
public class BuyController {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BuyRepository buyRepository;
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    TransactionRepository transactionRepository;

    private ResponseBuilder responseBuilder = new ResponseBuilder();

    @PostMapping("buy/{itemId}")
    public ResponseEntity buyItem(Model model, BuyForm form, @PathVariable Long itemId, @CookieValue(value="SessionID", defaultValue="") String sessionId) {
        Item item = itemRepository.findById(itemId).get();
        User user = sessionRepository.findUserBySessionCookie(sessionId);
        Buy buy = new Buy(item, form.getPurchasePrice(), "pending", user);
        buyRepository.save(buy);
        item.setAvailability(false);
        itemRepository.save(item);
        return responseBuilder.createSuccessResponse("Kaufanfrage wurde gestellt");
    }

    @PostMapping("buy/accept/{itemId}")
    public ResponseEntity itemSale(Model model, BuyForm form, @PathVariable Long itemId, @CookieValue(value="SessionID", defaultValue="") String sessionId) {
        Item item = itemRepository.findById(itemId).get();
        User user = sessionRepository.findUserBySessionCookie(sessionId);
        Buy buy = getPendingBuyOfItem(item);

        try {
            transferMoney(buy.getBuyer().getEmail(), user.getEmail(), buy.getPurchasePrice());
            Transaction transaction = new Transaction(buy.getBuyer(), user, buy.getPurchasePrice(), "Kauf von " + item.getName());
            item.setUser(buy.getBuyer());
            buy.setStatus("completed");
            itemRepository.save(item);
            buyRepository.save(buy);
            transactionRepository.save(transaction);
        } catch (Exception e) {
            return responseBuilder.createErrorResponse("Es war nicht möglich den Betrag zu überweisen. Bitte sende eine Email mit der genauen Beschreibung Deines Problems und dem Betreff \"" + sessionRepository.findUserBySessionCookie(sessionId).getUsername() + " - " + buy.getId().toString() + "\" an conflict@leihordie.de");
        }
        return responseBuilder.createSuccessResponse("Erfolgreich verkauft");
    }

    @PostMapping("buy/decline/{itemId}")
    public ResponseEntity declineItemSale(Model model, BuyForm form, @PathVariable Long itemId, @CookieValue(value="SessionID", defaultValue="") String sessionId) {
        Item item = itemRepository.findById(itemId).get();
        Buy buy = getPendingBuyOfItem(item);

        buy.setStatus("declined");

        buyRepository.save(buy);

        return responseBuilder.createSuccessResponse("Verkauf erfolgreich abgelehnt");
    }

    private Buy getPendingBuyOfItem(Item item) {
        List<Buy> buys = buyRepository.findBuysOfItem(item.getId());

        Buy buy = null;
        for (Buy b: buys) {
            if (b.getStatus().equals("pending")) {
                buy = b;
            }
        }

        return buy;
    }
}
