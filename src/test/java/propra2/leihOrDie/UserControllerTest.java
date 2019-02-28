package propra2.leihOrDie;


import org.junit.After;
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
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    UserRepository userRepository;

    @After
    public void tearDown() {
        sessionRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testShowUserPage() throws Exception {
        DummyUserGenerator dummyUserGenerator = new DummyUserGenerator();
        User user =dummyUserGenerator.generateUser();
        userRepository.save(user);

        DummyItemGenerator dummyItemGenerator = new DummyItemGenerator();
        Item item = dummyItemGenerator.generateItem(user);
        itemRepository.save(item);

        sessionRepository.save(new Session("1", user));

        mvc.perform(get("/myaccount")
                .cookie(new Cookie("SessionID", "1")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(model().attribute("user", user.getUsername()))
                .andExpect(model().attribute("items", hasItem(
                        allOf(
                                hasProperty("id", is(item.getId())),
                                hasProperty("cost", is(item.getCost())),
                                hasProperty("deposit", is(item.getDeposit())),
                                hasProperty("description", is(item.getDescription()))))));
    }

    @Test
    public void testUserNotLoggedIn() throws Exception {
        mvc.perform(get("/myaccount"))
                .andExpect(MockMvcResultMatchers
                        .redirectedUrl("/login"));
    }
}
