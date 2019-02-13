package propra2.leihOrDie;

import org.junit.Test;
import propra2.leihOrDie.model.Item;
import propra2.leihOrDie.model.User;
import java.security.*;

public class JoinTest {

    @Test
    public void testUserjoinItem() {
        User user = new User();
        Item item = new Item();
        byte[] thedigest;

        try {
            byte[] bytesOfMessage = "passwort1".getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            thedigest = md.digest(bytesOfMessage);
        } catch (java.io.UnsupportedEncodingException | java.security.NoSuchAlgorithmException e) {
            return;
        }

        user.setUsername("testuser");
        user.setEmail("test@email.de");
        user.setPassword(thedigest);

        item.setAvailability(true);
        item.setAvailableTime(10);
        item.setCost(100);
        item.setDeposit(20);
        item.setName("testitem");
        item.setDescription("ein test");
        item.setLocation("testlocation");
        item.setUser(user);
    }
}
