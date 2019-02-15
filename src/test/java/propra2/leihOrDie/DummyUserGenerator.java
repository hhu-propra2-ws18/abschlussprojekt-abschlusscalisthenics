package propra2.leihOrDie;

import propra2.leihOrDie.model.Address;
import propra2.leihOrDie.model.User;

import java.security.SecureRandom;
import java.util.UUID;

public class DummyUserGenerator {

    public User generateUser() {
        User user = new User();

        // Generating a unique username (ID) and unique email
        String username = UUID.randomUUID().toString();
        if (username.length() > 50) {
            String shortUsername = username.substring(0, 50);
            user.setUsername(shortUsername);
        }
        else
            user.setUsername(username);
        user.setEmail(UUID.randomUUID().toString());

        byte[] password = new byte[20];
        SecureRandom random = new SecureRandom();
        random.nextBytes(password);
        user.setPassword(password);

        user.setAddress(generateAddress());

        return user;
    }

    private Address generateAddress() {
        Address adr = new Address();
        adr.setCity("Düsseldorf");
        adr.setPostcode(40225);
        adr.setStreet("Universitätsstraße");
        adr.setHouseNumber(1);

        return adr;
    }
}
