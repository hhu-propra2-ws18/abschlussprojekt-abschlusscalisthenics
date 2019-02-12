package propra2.leihOrDie.dataaccess;

import org.springframework.data.repository.CrudRepository;
import propra2.leihOrDie.model.Picture;

import java.util.List;

public interface PictureRepository extends CrudRepository<Picture, Long> {
    List<Picture> findAll();
}
