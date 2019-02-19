package propra2.leihOrDie;

import org.junit.Test;
import org.junit.Assert;
import propra2.leihOrDie.model.Address;
import propra2.leihOrDie.model.User;

import java.security.MessageDigest;

public class UserTest {
    @Test
    public void testCreateUser() throws Exception {
        Address address = new Address(12345, "Teststr", 1, "Kuhlstadt");

        byte[] bytesOfMessage = "passwort1".getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] encryptedPw = md.digest(bytesOfMessage);

        CharSequence password = new StringBuffer("qwerty");

        User user = new User("testuser", "test@email.de", password, address);

        Assert.assertEquals(user.getUsername(), "testuser");
        Assert.assertEquals(user.getAddress(), address);
        Assert.assertEquals(user.getPassword(), encryptedPw);
        Assert.assertEquals(user.getEmail(), "test@email.de");

    }
}
