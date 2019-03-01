package propra2.leihOrDie;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import propra2.leihOrDie.propay.ProPayWrapper;

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

    @MockBean
    ProPayWrapper proPayWrapper;

    private User testUser1;
    private User testUser2;
    private User testAdmin;
    private Item testItem;
    private Loan testLoan;

    @Before
    public void setUp() throws Exception {
        DummyUserGenerator dummyUserGenerator = new DummyUserGenerator();
        testUser1 = dummyUserGenerator.generateUser();
        testUser2 = dummyUserGenerator.generateUser();
        testAdmin = dummyUserGenerator.generateUser();
        testAdmin.setRole("ADMIN");
        userRepository.save(testUser1);
        userRepository.save(testUser2);
        userRepository.save(testAdmin);

        Session userSession = new Session("1", testUser1);
        sessionRepository.save(userSession);

        Session adminSession = new Session("2", testAdmin);
        sessionRepository.save(adminSession);

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
    public void testSolveConflictLender() throws Exception {
        Mockito.doNothing()
                .when(proPayWrapper).punishAccount(testUser2.getEmail(), testLoan.getProPayReservationId());

        mvc.perform(post("/conflict/solve/" + testLoan.getId() + "/and/" + testUser1.getUsername())
                .cookie(new Cookie("SessionID", "2")))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assert.assertEquals("completed", loanRepository.findAll().get(0).getState());
    }

    @Test
    public void testSolveError() throws Exception {
        testLoan.setState("error");
        testLoan.setDuration(1);
        loanRepository.save(testLoan);

        testItem.setCost(10);
        itemRepository.save(testItem);

        Mockito.when(proPayWrapper.transferMoney(testUser2.getEmail(), testUser1.getEmail(), 10))
                .thenReturn("");

        mvc.perform(post("/conflict/solveerror/" + testLoan.getId())
                .cookie(new Cookie("SessionID", "2")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
