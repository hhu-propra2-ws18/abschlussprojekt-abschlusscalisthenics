package propra2.leihOrDie;

import org.junit.Test;
import propra2.leihOrDie.dataaccess.ItemRepository;
import propra2.leihOrDie.model.Item;
import propra2.leihOrDie.model.User;
import java.security.*;
import java.util.List;

public class JoinTest {

    @Test
    public void testUserjoinItem() throws Exception {
        byte[] bytesOfMessage = "passwort1".getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(bytesOfMessage);

        User user = new User("testuser", "test@email.de", thedigest);
        Item item = new Item("mäher", "ein test", 100, 20, true, 10,
                "testlocation", user);
        Item item2 = new Item("säge", "eine tolle säge", 200, 3, false, 20,
                "duesseldorf", user);

        List<Item> items = user.getItems();

        System.out.println(items.get(0));

    }
}
