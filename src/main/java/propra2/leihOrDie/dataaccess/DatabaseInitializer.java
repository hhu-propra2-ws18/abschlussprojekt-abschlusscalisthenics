package propra2.leihOrDie.dataaccess;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;
import propra2.leihOrDie.model.Address;
import propra2.leihOrDie.model.User;

import java.security.SecureRandom;
import java.util.Arrays;


@Component
public class DatabaseInitializer implements ServletContextInitializer {
    @Autowired
    UserRepository userRepository;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        User user1 = new User("anton", "anton@gmail.com", mkPassword(),
                new Address(12345, "Universitätsstr.", 12, "Düsseldorf"));
        User user2 = new User("max", "max@hotmail.com", mkPassword(),
                new Address(21357, "Hans-Wilhelm Str.", 45, "Hamburg"));
        User user3 = new User("hans", "hans@web.de", mkPassword(),
                new Address(21337, "Siegerstraße.", 23, "München"));
        User user4 = new User("lisa", "lisa@gmail.de", mkPassword(),
                new Address(80442, "Ehrenallee", 86, "Wolfsburg"));
        User user5 = new User("hanna", "hanna@gmx.de", mkPassword(),
                new Address(81422, "Karolingerstr.", 65, "Essen"));
        User user6 = new User("antonia", "antonia@live.de", mkPassword(),
                new Address(90761, "Bilker Allee", 63, "Aachen"));
        User user7 = new User("peter", "peter@aol.com", mkPassword(),
                new Address(12341, "Brunnenstraße", 35, "Lübeck"));
        User user8 = new User("petra", "petra@hhu.de", mkPassword(),
                new Address(67421, "Affenweg", 91, "Oberhausen"));
        User user9 = new User("otto", "otto@icloud.com", mkPassword(),
                new Address(90831, "Karl-Heinz Weg", 74, "Düsseldorf"));
        User user10 = new User("florian", "florian@web.de", mkPassword(),
                new Address(32452, "Heinrich-Heine-Allee", 3, "Nürnberg"));

        userRepository.saveAll(Arrays.asList(user1, user2, user3, user4, user5, user6, user7, user8,
                user9, user10));

    }

    private byte[] mkPassword() {
        byte[] bytes = new byte[20];
        try {
            SecureRandom.getInstanceStrong().nextBytes(bytes);
        } catch (java.security.NoSuchAlgorithmException e) {

        }
        return bytes;
    }
}
