package propra2.leihOrDie;

import org.junit.*;
import org.junit.runner.RunWith;
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

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class LoanControllerTest {
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

        Session userSession1 = new Session("1", testUser1);
        sessionRepository.save(userSession1);

        Session userSession2 = new Session("2", testUser2);
        sessionRepository.save(userSession2);

        Session adminSession = new Session("3", testAdmin);
        sessionRepository.save(adminSession);

        DummyItemGenerator dummyItemGenerator = new DummyItemGenerator();
        testItem = dummyItemGenerator.generateItem(testUser2);
        testItem.setAvailableTime(5);
        testItem.setDeposit(50);
        testItem.setCost(10);
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

    @Ignore
    @Test
    public void testRequestLoan() throws Exception {
        when(
                        proPayWrapper.reserve(testUser1.getEmail()
                                ,testItem.getUser().getEmail()
                                ,testItem.getDeposit())
                                .getId())
                .thenReturn(1L);

        mvc.perform(post("/request/" + testItem.getId())
                .cookie(new Cookie("SessionID", "1"))
                .param("loanDuration", "2"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assert.assertEquals(loanRepository.findLoansOfUser(testUser1.getUsername()).size(),1);
    }

    @Test
    public void testChangeStatusToAccepted() throws Exception {
        testLoan.setState("active");
        loanRepository.save(testLoan);

        mvc.perform(post("/request/accept/" + testLoan.getId())
                .cookie(new Cookie("SessionID", "2")))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assert.assertEquals("accepted", loanRepository.findLoansOfUser(testUser2.getUsername()).get(0).getState());
    }

    @Test
    public void testChangeStatusToDeclined() throws Exception {
        testLoan.setState("active");
        loanRepository.save(testLoan);

        mvc.perform(post("/request/decline/" + testLoan.getId())
                .cookie(new Cookie("SessionID", "2")))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assert.assertEquals("declined", loanRepository.findLoansOfUser(testUser2.getUsername()).get(0).getState());
        Assert.assertTrue(loanRepository.findLoansOfUser(testUser2.getUsername()).get(0).getItem().isAvailability());
    }

    @Test
    public void testChangeStatusToActive() throws Exception {
        testLoan.setState("declined");
        loanRepository.save(testLoan);

        mvc.perform(post("/request/activate/" + testLoan.getId())
                .cookie(new Cookie("SessionID", "2")))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assert.assertEquals("active", loanRepository.findLoansOfUser(testUser2.getUsername()).get(0).getState());
    }

    @Ignore
    @Test
    public void testChangeStatusToCompleted() throws Exception {
        testLoan.setState("active");
        testLoan.setDuration(2);
        loanRepository.save(testLoan);

        doNothing().when(proPayWrapper.transferMoney(testUser1.getEmail(), testUser2.getEmail(),20));

        mvc.perform(post("/request/return/" + testLoan.getId())
                .cookie(new Cookie("SessionID", "2")))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assert.assertEquals("completed", loanRepository.findLoansOfUser(testUser2.getUsername()).get(0).getState());
    }
}
