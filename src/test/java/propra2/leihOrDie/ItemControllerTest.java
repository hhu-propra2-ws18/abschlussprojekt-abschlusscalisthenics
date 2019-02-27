package propra2.leihOrDie;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import propra2.leihOrDie.dataaccess.ItemRepository;
import propra2.leihOrDie.dataaccess.PictureRepository;
import propra2.leihOrDie.dataaccess.SessionRepository;
import propra2.leihOrDie.dataaccess.UserRepository;
import propra2.leihOrDie.model.Item;
import propra2.leihOrDie.model.Session;
import propra2.leihOrDie.model.User;

import javax.servlet.http.Cookie;

import java.util.List;
import java.util.regex.Matcher;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class ItemControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    PictureRepository pictureRepository;

    @After
    public void tearDown() {
        pictureRepository.deleteAll();
        itemRepository.deleteAll();
        sessionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testCreateItem() throws Exception {
        DummyUserGenerator dummyUserGenerator = new DummyUserGenerator();
        User user = dummyUserGenerator.generateUser();
        userRepository.save(user);

        sessionRepository.save(new Session("1", user));
        DummyItemGenerator dummyItemGenerator = new DummyItemGenerator();
        Item item = dummyItemGenerator.generateItem(user);
        itemRepository.save(item);

        mvc.perform(post("/item/create")
                .cookie(new Cookie("SessionID", "1"))
                .param("name", "Teller")
                .param("description", "Rotes Uhralter Teller (Jahr 10000000000 BC)")
                .param("cost", "99999")
                .param("deposit", "1000")
                .param("availableTime", "5"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/item/" + (item.getId()+1) + "/uploadphoto"));

        Assert.assertEquals(itemRepository.count(), 2);
        Assert.assertEquals(itemRepository.findById(item.getId()+1).get().getName(), "Teller");
    }

    @Test
    public void testEditItem() throws Exception {
        DummyUserGenerator dummyUserGenerator = new DummyUserGenerator();
        User user = dummyUserGenerator.generateUser();
        userRepository.save(user);

        sessionRepository.save(new Session("1", user));
        DummyItemGenerator dummyItemGenerator = new DummyItemGenerator();
        Item item = dummyItemGenerator.generateItem(user);
        itemRepository.save(item);

        mvc.perform(post("/item/edit/" + item.getId())
                .cookie(new Cookie("SessionID", "1"))
                .param("name", "Fahrrad")
                .param("description", "Toller Fahrrad!")
                .param("cost", "50")
                .param("deposit", "200")
                .param("availableTime", "10"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/borrowall/" + item.getId() + "/"));

        Assert.assertEquals(itemRepository.count(), 1);

        // Name attribute was not changed
        Assert.assertEquals(itemRepository.findById(item.getId()).get().getName(), "Fahrrad");
        // Description attribute was changed
        Assert.assertEquals(itemRepository.findById(item.getId()).get().getDescription(), "Toller Fahrrad!");
        // Deposit attribute was changed
        Assert.assertEquals(itemRepository.findById(item.getId()).get().getDeposit(), 200);
    }


    @Test
    public void testShowItem() throws Exception {
        DummyUserGenerator dummyUserGenerator = new DummyUserGenerator();
        User user = dummyUserGenerator.generateUser();
        userRepository.save(user);

        sessionRepository.save(new Session("1", user));
        DummyItemGenerator dummyItemGenerator = new DummyItemGenerator();
        Item item = dummyItemGenerator.generateItem(user);
        itemRepository.save(item);

        mvc.perform(get("/borrowall/" + item.getId() + "/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(model().attribute("description", item.getDescription()))
                .andExpect(model().attribute("location", item.getUser().getAddress().getCity()))
                .andExpect(model().attribute("availableTime", item.getAvailableTime()))
                .andExpect(model().attribute("deposit", item.getDeposit()))
                .andExpect(model().attribute("cost", item.getCost()))
                .andExpect(model().attribute("username", item.getUser().getUsername()));
    }

    @Test
    public void testListAllItemsOnlyOneItem() throws Exception {
        DummyUserGenerator dummyUserGenerator = new DummyUserGenerator();
        User user = dummyUserGenerator.generateUser();
        userRepository.save(user);

        sessionRepository.save(new Session("1", user));
        DummyItemGenerator dummyItemGenerator = new DummyItemGenerator();
        Item item1 = dummyItemGenerator.generateItem(user);
        itemRepository.save(item1);
        Item item2 = dummyItemGenerator.generateAnotherItem(user);itemRepository.save(item2);

        mvc.perform(get("/borrowall/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(model().attribute("items", hasSize(2)))
                .andExpect(model().attribute("items", hasItem(
                        allOf(
                                hasProperty("id", is(item1.getId())),
                                hasProperty("cost", is(item1.getCost())),
                                hasProperty("deposit", is(item1.getDeposit()))))))
                .andExpect(model().attribute("items", hasItem(
                        allOf(
                                hasProperty("id", is(item2.getId())),
                                hasProperty("cost", is(item2.getCost())),
                                hasProperty("deposit", is(item2.getDeposit()))))));
    }

    @Test
    public void testListAllItemsTwoItemsExist() throws Exception {
        DummyUserGenerator dummyUserGenerator = new DummyUserGenerator();
        User user = dummyUserGenerator.generateUser();
        userRepository.save(user);

        sessionRepository.save(new Session("1", user));
        DummyItemGenerator dummyItemGenerator = new DummyItemGenerator();
        Item item1 = dummyItemGenerator.generateItem(user);
        Item item2 = dummyItemGenerator.generateAnotherItem(user);
        itemRepository.save(item1);
        itemRepository.save(item2);

        mvc.perform(get("/borrowall/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(model().attribute("items", hasSize(2)));
    }
}
