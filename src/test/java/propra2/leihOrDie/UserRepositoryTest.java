package propra2.leihOrDie;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import propra2.leihOrDie.dataaccess.UserRepository;
import propra2.leihOrDie.model.User;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    public void testSaveOneUser() {
        User user = (new DummyUserGenerator()).generateUser();

        userRepository.save(user);
        List<User> userList = userRepository.findAll();

        Assert.assertEquals(1, userList.size());

        userRepository.deleteAll();
    }

    @Test
    public void testFindOneSavedUserByEmail() {
        User user = (new DummyUserGenerator()).generateUser();

        userRepository.save(user);
        List<User> userList = userRepository.findUserByEMail(user.getEmail());

        Assert.assertEquals(1, userList.size());

        userRepository.deleteAll();
    }

    @Test
    public void testDontFindNotSavedUserByEmail() {
        User user = (new DummyUserGenerator()).generateUser();

        userRepository.save(user);
        List<User> userList = userRepository.findUserByEMail("not@existent.io");

        Assert.assertEquals(0, userList.size());

        userRepository.deleteAll();

    }
}
