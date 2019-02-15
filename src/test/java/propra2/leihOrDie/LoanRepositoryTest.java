package propra2.leihOrDie;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import propra2.leihOrDie.dataaccess.ItemRepository;
import propra2.leihOrDie.dataaccess.LoanRepository;
import propra2.leihOrDie.dataaccess.UserRepository;
import propra2.leihOrDie.model.Item;
import propra2.leihOrDie.model.User;
import propra2.leihOrDie.model.Loan;

import java.util.List;

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
        //loanRepository.deleteAll();
    }

    /*
    @Test
    public void testCreatLoan() throws Exception {
        Assertions.assertThat(1).isEqualTo(1);
    }
    */

    @Test
    public void saveOneLoan() throws Exception {
        DummyUserGenerator dummyUserGenerator = new DummyUserGenerator();
        DummyItemGenerator dummyItemGenerator = new DummyItemGenerator();
        DummyLoanGenerator dummyLoanGenerator = new DummyLoanGenerator();

        User firstUser = dummyUserGenerator.generateUser();
        User secondUser = dummyUserGenerator.generateUser();
        secondUser.setUsername("Julia");
        firstUser.setUsername("Julian");
        Item item = dummyItemGenerator.generateItem(firstUser);
        Loan loan = dummyLoanGenerator.generateLoan2(item, secondUser);

        userRepository.save(firstUser);
        userRepository.save(secondUser);
        itemRepository.save(item);
        loanRepository.save(loan);

        List<Loan> loanList = loanRepository.findAll();

        Assertions.assertThat(loanList.size()).isEqualTo(1);
        Assertions.assertThat(loanList.get(0).getItem().getName()).isEqualTo("Fahrrad");
        Assertions.assertThat(loanList.get(0).getUser().getUsername()).isEqualTo("Julia");
        Assertions.assertThat(loanList.get(0).getItem().getUser().getUsername()).isEqualTo("Julian");
    }

    @Test
    public void saveSeveralLoans() throws Exception {
        DummyUserGenerator dummyUserGenerator = new DummyUserGenerator();
        DummyItemGenerator dummyItemGenerator = new DummyItemGenerator();
        DummyLoanGenerator dummyLoanGenerator = new DummyLoanGenerator();

        User firstUser = dummyUserGenerator.generateUser();
        User secondUser = dummyUserGenerator.generateUser();
        Item firstItem = dummyItemGenerator.generateItem(firstUser);
        Item secondItem = dummyItemGenerator.generateAnotherItem(firstUser);
        Loan firstLoan = dummyLoanGenerator.generateLoan2(firstItem, secondUser);
        Loan secondLoan = dummyLoanGenerator.generateLoan2(secondItem, secondUser);

        userRepository.save(firstUser);
        userRepository.save(secondUser);
        itemRepository.save(firstItem);
        itemRepository.save(secondItem);
        loanRepository.save(firstLoan);
        loanRepository.save(secondLoan);

        List<Loan> loanList = loanRepository.findAll();

        Assertions.assertThat(loanList.size()).isEqualTo(2);
        Assertions.assertThat(loanList.get(0).getItem().getName()).isEqualTo("Fahrrad");
        Assertions.assertThat(loanList.get(1).getItem().getName()).isEqualTo("Kickbike");
    }

    @Test
    public void testAllLoansOfAUser() throws Exception {
        DummyUserGenerator dummyUserGenerator = new DummyUserGenerator();
        DummyItemGenerator dummyItemGenerator = new DummyItemGenerator();
        DummyLoanGenerator dummyLoanGenerator = new DummyLoanGenerator();

        User firstUser = dummyUserGenerator.generateUser();
        User secondUser = dummyUserGenerator.generateUser();
        Item firstItem = dummyItemGenerator.generateItem(firstUser);
        Item secondItem = dummyItemGenerator.generateAnotherItem(firstUser);
        Loan firstLoan = dummyLoanGenerator.generateLoan2(firstItem, secondUser);
        Loan secondLoan = dummyLoanGenerator.generateLoan2(secondItem, secondUser);

        userRepository.save(firstUser);
        userRepository.save(secondUser);
        itemRepository.save(firstItem);
        itemRepository.save(secondItem);
        loanRepository.save(firstLoan);
        loanRepository.save(secondLoan);

        List<Loan> loansOfFirstUser = loanRepository.findLoansOfUser(firstUser.getUsername());
        Assertions.assertThat(loansOfFirstUser.size()).isEqualTo(0);
        List<Loan> loansOfsecondUser = loanRepository.findLoansOfUser(secondUser.getUsername());
        Assertions.assertThat(loansOfsecondUser.size()).isEqualTo(2);
    }

    @Test
    public void testAllLoansOfAnItem() throws Exception {
        DummyUserGenerator dummyUserGenerator = new DummyUserGenerator();
        DummyItemGenerator dummyItemGenerator = new DummyItemGenerator();
        DummyLoanGenerator dummyLoanGenerator = new DummyLoanGenerator();

        User firstUser = dummyUserGenerator.generateUser();
        User secondUser = dummyUserGenerator.generateUser();
        Item firstItem = dummyItemGenerator.generateItem(firstUser);
        Item secondItem = dummyItemGenerator.generateItem(firstUser);
        Loan firstLoan = dummyLoanGenerator.generateLoan2(firstItem, secondUser);
        Loan secondLoan = dummyLoanGenerator.generateLoan2(firstItem, secondUser);

        userRepository.save(firstUser);
        userRepository.save(secondUser);
        itemRepository.save(firstItem);
        itemRepository.save(secondItem);
        loanRepository.save(firstLoan);
        loanRepository.save(secondLoan);

        List<Loan> loansFirstItem = loanRepository.findLoansOfItem(firstItem.getId());
        Assertions.assertThat(loansFirstItem.size()).isEqualTo(2);
        List<Loan> loansSecondItem = loanRepository.findLoansOfItem(secondItem.getId());
        Assertions.assertThat(loansSecondItem.size()).isEqualTo(0);
    }
}