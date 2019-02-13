package propra2.leihOrDie;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import propra2.leihOrDie.dataaccess.PictureRepository;
import propra2.leihOrDie.model.Picture;

import java.sql.Blob;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PictureRepositoryTest {

    @Autowired
    PictureRepository repo;

    @After
    public void tearDown() {
        repo.deleteAll();
    }

    @Test
    public void saveOnePicture() {

    }
}
