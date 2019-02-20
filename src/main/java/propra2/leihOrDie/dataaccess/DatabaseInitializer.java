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
        User user1 = new User("anton", "anton@gmail.com", "anton1234",
                new Address(12345, "Universitaetsstr.", 12, "Duesseldorf"));
        User user2 = new User("max", "max@hotmail.com", "max1234",
                new Address(21357, "Hans-Wilhelm Str.", 45, "Hamburg"));
        User user3 = new User("hans", "hans@web.de", "hans1234",
                new Address(21337, "Siegerstrasse", 23, "Muenchen"));
        User user4 = new User("lisa", "lisa@gmail.de", "lisa1234",
                new Address(80442, "Ehrenallee", 86, "Wolfsburg"));
        User user5 = new User("hanna", "hanna@gmx.de", "hanna1234",
                new Address(81422, "Karolingerstr.", 65, "Essen"));
        User user6 = new User("antonia", "antonia@live.de","antonia1234",
                new Address(90761, "Bilker Allee", 63, "Aachen"));
        User user7 = new User("peter", "peter@aol.com", "peter1234",
                new Address(12341, "Brunnenstraße", 35, "Luebeck"));
        User user8 = new User("petra", "petra@hhu.de", "petra1234",
                new Address(67421, "Affenweg", 91, "Oberhausen"));
        User user9 = new User("otto", "otto@icloud.com", "otto1234",
                new Address(90831, "Karl-Heinz Weg", 74, "Duesseldorf"));
        User user10 = new User("florian", "florian@web.de", "florian1234",
                new Address(32452, "Heinrich-Heine-Allee", 3, "Nuernberg"));

        userRepository.saveAll(Arrays.asList(user1, user2, user3, user4, user5, user6, user7, user8,
                user9, user10));

    }
}
