package propra2.leihOrDie;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import propra2.leihOrDie.dataaccess.ItemRepository;
import propra2.leihOrDie.dataaccess.UserRepository;
import propra2.leihOrDie.model.Item;
import propra2.leihOrDie.model.User;
import java.security.*;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JoinTest {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

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

        //user.setItems(Arrays.asList(item, item2));

        userRepository.save(user);
        itemRepository.save(item);
        itemRepository.save(item2);

        User userFromDB = userRepository.findById("testuser").get();

        List<Item> items = itemRepository.findItemsFromUser("testuser");

        System.out.println(items.size());

    }
}
