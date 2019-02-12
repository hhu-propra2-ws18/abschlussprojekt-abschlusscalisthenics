package propra2.leihOrDie.dataaccess;

import org.springframework.data.repository.CrudRepository;
import propra2.leihOrDie.model.Item;

import java.util.List;

public interface ItemRepository extends CrudRepository<Item, Long> {
    List<Item> findAll();
}
