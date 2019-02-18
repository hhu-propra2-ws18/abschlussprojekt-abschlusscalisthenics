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
    public void testLoan() throws Exception {
        Address address = new Address(12345, "Teststr", 1, "Kuhlstadt");

        byte[] bytesOfMessage = "passwort1".getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] encryptedPw = md.digest(bytesOfMessage);

        User user = new User("testuser", "test@email.de", encryptedPw, address);
        Item item = new Item("testitem", "das ist ein test", 200, 10,
                true, 20, user);

        Loan loan = new Loan("abgeschlossen", 19, user, item);

        Assert.assertEquals("abgeschlossen", loan.getState());
        Assert.assertEquals(19, loan.getDuration());
        Assert.assertEquals(user, loan.getUser());
        Assert.assertEquals(item, loan.getItem());
    }
}
