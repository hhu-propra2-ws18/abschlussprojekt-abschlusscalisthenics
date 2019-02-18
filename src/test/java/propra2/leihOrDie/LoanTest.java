package propra2.leihOrDie;

import org.junit.Assert;
import org.junit.Test;
import propra2.leihOrDie.model.Address;
import propra2.leihOrDie.model.Item;
import propra2.leihOrDie.model.Loan;
import propra2.leihOrDie.model.User;

import java.security.MessageDigest;

public class LoanTest {
    @Test
    public void testLoan() {
        User user = (new DummyUserGenerator()).generateUser();
        Item item = (new DummyItemGenerator()).generateItem(user);

        Loan loan = new Loan("abgeschlossen", 19, user, item);

        Assert.assertEquals("abgeschlossen", loan.getState());
        Assert.assertEquals(19, loan.getDuration());
        Assert.assertEquals(user, loan.getUser());
        Assert.assertEquals(item, loan.getItem());
    }
}
