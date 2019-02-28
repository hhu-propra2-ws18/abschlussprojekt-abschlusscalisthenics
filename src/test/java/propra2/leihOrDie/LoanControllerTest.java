package propra2.leihOrDie;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import propra2.leihOrDie.dataaccess.*;
import propra2.leihOrDie.model.Item;
import propra2.leihOrDie.model.Session;
import propra2.leihOrDie.model.User;

import javax.servlet.http.Cookie;

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
    PictureRepository pictureRepository;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    LoanRepository loanRepository;

    @After
    public void tearDown() {
        pictureRepository.deleteAll();
        loanRepository.deleteAll();
        sessionRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testRequestLoan() throws Exception {
        DummyUserGenerator dummyUserGenerator = new DummyUserGenerator();
        User user1 = dummyUserGenerator.generateUser();
        User user2 = dummyUserGenerator.generateUser();
        userRepository.save(user1);
        userRepository.save(user2);
        sessionRepository.save(new Session("1", user2));
        DummyItemGenerator dummyItemGenerator = new DummyItemGenerator();
        Item item = dummyItemGenerator.generateItem(user1);
        item.setAvailableTime(5);
        itemRepository.save(item);

        mvc.perform(post("/request/" + item.getId())
                .cookie(new Cookie("SessionID", "1"))
                .param("loanDuration", "3"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //Assert.assertEquals(loanRepository.findLoansOfUser(user2.getUsername()).size(), 1);
    }
}
