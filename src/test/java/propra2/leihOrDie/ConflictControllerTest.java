package propra2.leihOrDie;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import propra2.leihOrDie.dataaccess.ItemRepository;
import propra2.leihOrDie.dataaccess.LoanRepository;
import propra2.leihOrDie.dataaccess.SessionRepository;
import propra2.leihOrDie.dataaccess.UserRepository;
import propra2.leihOrDie.model.Item;
import propra2.leihOrDie.model.Loan;
import propra2.leihOrDie.model.Session;
import propra2.leihOrDie.model.User;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class ConflictControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    SessionRepository sessionRepository;

    private User testUser1;
    private User testUser2;
    private Item testItem;
    private Loan testLoan;

    @Before
    public void setUp() throws Exception {
        DummyUserGenerator dummyUserGenerator = new DummyUserGenerator();
        testUser1 = dummyUserGenerator.generateUser();
        testUser2 = dummyUserGenerator.generateUser();
        userRepository.save(testUser1);
        userRepository.save(testUser2);

        Session session = new Session("1", testUser1);
        sessionRepository.save(session);

        DummyItemGenerator dummyItemGenerator = new DummyItemGenerator();
        testItem = dummyItemGenerator.generateItem(testUser1);
        itemRepository.save(testItem);

        DummyLoanGenerator dummyLoanGenerator = new DummyLoanGenerator();
        testLoan = dummyLoanGenerator.generateLoan(testItem, testUser2);
        loanRepository.save(testLoan);
    }

    @After
    public void tearDown() {
        loanRepository.deleteAll();
        itemRepository.deleteAll();
        sessionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testOpenConflict() throws Exception {
        testLoan.setState("accepted");
        loanRepository.save(testLoan);

        mvc.perform(post("/conflict/open/" + testLoan.getId())
        .cookie(new Cookie("SessionID", "1")))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assert.assertEquals("conflict", loanRepository.findAll().get(0).getState());
        Assert.assertFalse(itemRepository.findAll().get(0).isAvailability());
    }

    @Test
    public void testSolveConflict() throws Exception {
        
    }
}
