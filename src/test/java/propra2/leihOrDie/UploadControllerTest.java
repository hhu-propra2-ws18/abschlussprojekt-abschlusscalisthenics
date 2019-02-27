package propra2.leihOrDie;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import propra2.leihOrDie.dataaccess.ItemRepository;
import propra2.leihOrDie.dataaccess.PictureRepository;
import propra2.leihOrDie.dataaccess.SessionRepository;
import propra2.leihOrDie.dataaccess.UserRepository;
import propra2.leihOrDie.model.*;
import javax.servlet.http.Cookie;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class UploadControllerTest {

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

    @Before
    public void setUp() {
        String password= "password";
        Address address = new Address(1337, "TestStreet", 42, "TestCity");
        User testUser = new User("name", "email@test.de", password, "USER", address);
        userRepository.save(testUser);

        Item testItem = new Item("name", "description", 314, 1, true, 1, testUser.getAddress(), testUser, 0);
        itemRepository.save(testItem);

        sessionRepository.save(new Session("1", testUser));
    }

    @After
    public void tearDown() {
        List<Picture> pictureList = pictureRepository.findAll();
        if (pictureList.size() == 1) {
            String pictureId = Long.toString(pictureList.get(0).getId());
            String type = pictureList.get(0).getType();
            File file = new File("img/" + pictureId.concat(type));
            file.delete();
        }

        pictureRepository.deleteAll();
        sessionRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testUploadImage() throws Exception {
        byte[] bytes = Files.readAllBytes(Paths.get("img/", "test.jpg"));
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", bytes);

        mvc.perform(multipart("/item/1/uploadphoto")
                .file(file)
                .cookie(new Cookie("SessionID", "1")))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/item/1/uploadphoto"))
                .andExpect(flash().attribute("message", "Sie haben schon 1 Fotos hochgeladen"));

        Assert.assertEquals(pictureRepository.findPicturesOfItem(1L).size(), 1);
    }
    @Test
    public void testNoFileMessage() throws Exception {
        byte[] bytes = {};
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", bytes);

        mvc.perform(multipart("/item/1/uploadphoto")
                .file(file)
                .cookie(new Cookie("SessionID", "1")))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/item/1/uploadphoto"))
                .andExpect(flash().attribute("message", "Bitte eine Datei wählen :)"));

        Assert.assertEquals(pictureRepository.findPicturesOfItem(1L).size(), 0);
    }

}
