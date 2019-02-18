package propra2.leihOrDie;


import org.junit.Assert;
import org.junit.Test;
import propra2.leihOrDie.model.Address;
import propra2.leihOrDie.model.Item;
import propra2.leihOrDie.model.User;

import java.security.MessageDigest;

public class ItemTest {
    @Test
    public void testPicture() throws Exception {
        Address address = new Address(12345, "Teststr", 1, "Kuhlstadt");

        byte[] bytesOfMessage = "passwort1".getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] encryptedPw = md.digest(bytesOfMessage);

        User user = new User("testuser", "test@email.de", encryptedPw, address);
        Item item = new Item("testitem", "das ist ein test", 200, 10,
                true, 20, user);

        Assert.assertEquals(item.getName(), "testitem");
        Assert.assertEquals(item.getDescription(), "das ist ein test");
        Assert.assertEquals(item.getCost(), 200);
        Assert.assertEquals(item.getDeposit(), 10);
        Assert.assertEquals(item.getAvailableTime(), 20);
        Assert.assertTrue(item.isAvailability());
    }

}
