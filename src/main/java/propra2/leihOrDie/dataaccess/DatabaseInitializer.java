package propra2.leihOrDie.dataaccess;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;
import propra2.leihOrDie.model.*;

import java.util.Arrays;

import static propra2.leihOrDie.propay.ProPayWrapper.raiseBalanceOfUser;


//@Component
public class DatabaseInitializer implements ServletContextInitializer {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        User user0 = new User("admin", "admin@leihordie.de", "admin1234", "ADMIN",
                new Address(12345, "Universitaetsstr.", 12, "Duisburg"));
        User user1 = new User("anton", "anton@gmail.com", "anton1234", "USER",
                new Address(12345, "Universitaetsstr.", 12, "Duesseldorf"));
        User user2 = new User("max", "max@hotmail.com", "max1234", "USER",
                new Address(21357, "Hans-Wilhelm Str.", 45, "Hamburg"));
        User user3 = new User("hans", "hans@web.de", "hans1234", "USER",
                new Address(21337, "Siegerstrasse", 23, "Muenchen"));
        User user4 = new User("lisa", "lisa@gmail.de", "lisa1234", "USER",
                new Address(80442, "Ehrenallee", 86, "Wolfsburg"));
        User user5 = new User("hanna", "hanna@gmx.de", "hanna1234", "USER",
                new Address(81422, "Karolingerstr.", 65, "Essen"));
        User user6 = new User("antonia", "antonia@live.de","antonia1234", "USER",
                new Address(90761, "Bilker Allee", 63, "Aachen"));
        User user7 = new User("peter", "peter@aol.com", "peter1234", "USER",
                new Address(12341, "Brunnenstraße", 35, "Luebeck"));
        User user8 = new User("petra", "petra@hhu.de", "petra1234", "USER",
                new Address(67421, "Affenweg", 91, "Oberhausen"));
        User user9 = new User("otto", "otto@icloud.com", "otto1234", "USER",
                new Address(90831, "Karl-Heinz Weg", 74, "Duesseldorf"));
        User user10 = new User("florian", "florian@web.de", "florian1234", "USER",
                new Address(32452, "Heinrich-Heine-Allee", 3, "Nuernberg"));

        userRepository.saveAll(Arrays.asList(user0, user1, user2, user3, user4, user5, user6, user7, user8,
                user9, user10));

        raiseBalanceOfUser(user1.getEmail(), 10000);
        Transaction transaction1 = new Transaction(user1, user1, 10000, "Überweisung");
        raiseBalanceOfUser(user2.getEmail(), 10000);
        Transaction transaction2 = new Transaction(user2, user2, 10000, "Überweisung");
        raiseBalanceOfUser(user3.getEmail(), 10000);
        Transaction transaction3 = new Transaction(user3, user3, 10000, "Überweisung");
        raiseBalanceOfUser(user4.getEmail(), 10000);
        Transaction transaction4 = new Transaction(user4, user4, 10000, "Überweisung");
        raiseBalanceOfUser(user5.getEmail(), 10000);
        Transaction transaction5 = new Transaction(user5, user5, 10000, "Überweisung");
        raiseBalanceOfUser(user6.getEmail(), 10000);
        Transaction transaction6 = new Transaction(user6, user6, 10000, "Überweisung");
        raiseBalanceOfUser(user7.getEmail(), 10000);
        Transaction transaction7 = new Transaction(user7, user7, 10000, "Überweisung");
        raiseBalanceOfUser(user8.getEmail(), 10000);
        Transaction transaction8 = new Transaction(user8, user8, 10000, "Überweisung");
        raiseBalanceOfUser(user9.getEmail(), 10000);
        Transaction transaction9 = new Transaction(user9, user9, 10000, "Überweisung");
        raiseBalanceOfUser(user10.getEmail(), 10000);
        Transaction transaction10 = new Transaction(user10, user10, 10000, "Überweisung");

        transactionRepository.saveAll(Arrays.asList(transaction1, transaction2, transaction3, transaction4, transaction5, transaction6, transaction7, transaction8, transaction9, transaction10));

        Item item1 = new Item("Säge", "Sehr scharf", 5, 30, true, 4, user1.getAddress(), user1, -1);
        Item item2 = new Item("Hammer", "Super schwer", 2, 15, true, 3, user2.getAddress(), user2, 50);
        Item item3 = new Item("Rasenmäher", "Schön schnell", 10, 60, true, 2, user6.getAddress(), user6, 100);
        Item item4 = new Item("Stift", "schreibt sehr angenehm", 1, 3, true, 6, user1.getAddress(), user1, -1);
        Item item5 = new Item("Fahrrad", "Quitscht beim Fahren", 6, 50, true, 10, user2.getAddress(), user2, 80);
        Item item6 = new Item("Whiteboard", "Super cool", 4, 40, true, 3, user1.getAddress(), user1, -1);

        itemRepository.saveAll(Arrays.asList(item1, item2, item3, item4, item5, item6));

        Long propayReservationId = new Long(23567);
        Loan loan1 = new Loan("pending", 2, user7, item1, propayReservationId);
        Loan loan2 = new Loan("accepted", 1, user8, item4, propayReservationId);
        Loan loan3 = new Loan("pending", 1, user9, item2, propayReservationId);
        Loan loan4 = new Loan("pending", 2, user5, item6, propayReservationId);

        loanRepository.saveAll(Arrays.asList(loan1, loan2, loan3, loan4));
    }
}
