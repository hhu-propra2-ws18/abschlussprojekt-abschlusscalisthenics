package propra2.leihOrDie;

import org.junit.Test;
import propra2.leihOrDie.model.Item;
import propra2.leihOrDie.model.User;
import java.security.*;

public class JoinTest {

    @Test
    public void testUserjoinItem() throws Exception {
        byte[] bytesOfMessage = "passwort1".getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(bytesOfMessage);

        User user = new User("testuser", "test@email.de", thedigest);

        Item item = new Item("testitem", "ein test", 100, 20, true, 10,
                "testlocation", user);

    }
}
