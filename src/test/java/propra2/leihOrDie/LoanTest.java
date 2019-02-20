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

<<<<<<< HEAD
        Loan loan = new Loan("abgeschlossen", 19, user, item, 20);
=======
        CharSequence clearPassword = new StringBuffer("SuperSave");

        User user = new User("testuser", "test@email.de", clearPassword, address);
        Item item = new Item("testitem", "das ist ein test", 200, 10,
                true, 20, user);

        Loan loan = new Loan("abgeschlossen", 19, user, item);
>>>>>>> 8e6ae6568cff0f4ecd3590f4e6941630d926399e

        Assert.assertEquals("abgeschlossen", loan.getState());
        Assert.assertEquals(19, loan.getDuration());
        Assert.assertEquals(user, loan.getUser());
        Assert.assertEquals(item, loan.getItem());
        Assert.assertEquals(20, loan.getPropayReservationId());
    }
}
