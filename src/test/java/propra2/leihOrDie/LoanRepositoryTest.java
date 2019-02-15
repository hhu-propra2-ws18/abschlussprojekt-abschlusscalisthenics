package propra2.leihOrDie;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import propra2.leihOrDie.dataaccess.ItemRepository;
import propra2.leihOrDie.dataaccess.LoanRepository;
import propra2.leihOrDie.dataaccess.UserRepository;
import propra2.leihOrDie.model.Loan;
import propra2.leihOrDie.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoanRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LoanRepository loanRepository;

    @After
    public void deleteAllTestItems() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
        loanRepository.deleteAll();
    }

    @Test
    public void testCreatLoan() throws Exception {

    }

    @Test
    public void saveOneLoan() {
        DummyUserGenerator dummyUserGenerator = new DummyUserGenerator();
        DummyItemGenerator dummyItemGenerator = new DummyItemGenerator();
        DummyLoanGenerator dummyLoanGenerator = new DummyLoanGenerator();

        Loan loan = dummyLoanGenerator.generateLoan();
        User user = loan.getUser();
    }
}
