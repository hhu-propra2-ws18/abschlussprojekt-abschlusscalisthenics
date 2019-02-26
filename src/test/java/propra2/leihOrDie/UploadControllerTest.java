package propra2.leihOrDie;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import propra2.leihOrDie.dataaccess.ItemRepository;
import propra2.leihOrDie.dataaccess.PictureRepository;
import propra2.leihOrDie.dataaccess.SessionRepository;
import propra2.leihOrDie.dataaccess.UserRepository;
import propra2.leihOrDie.model.*;

import javax.servlet.http.Cookie;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

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

        Item testItem = new Item("name", "description", 314, 1, true, 1, testUser.getAddress(), testUser);
        itemRepository.save(testItem);

        sessionRepository.save(new Session("1", testUser));
    }

    @After
    public void tearDown() {
        List<Picture> pictureList = pictureRepository.findAll();
        if (!pictureList.isEmpty()) {
            String pictureId = Long.toString(pictureList.get(0).getId());
            File file = new File("images/" + pictureId + ".jpg");

            file.delete();

        }

        pictureRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();

    }

    @Test
    public void testUploadImage() throws Exception {
        FileInputStream fis = new FileInputStream("src/main/resources/images/test.jpg");
        MockMultipartFile multipartFile = new MockMultipartFile("file", fis);

        List<Item> itemList = itemRepository.findAll();
        String itemId = Long.toString(itemList.get(0).getId());

        String fileName = "test.jpg";

        mvc.perform(MockMvcRequestBuilders.multipart("/image/upload")
                .file(multipartFile)
                .cookie(new Cookie("SessionID", "1"))
                .param("itemId", itemId)
                .param("fileName", fileName))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/image/uploadSuccessful"));
    }
}
