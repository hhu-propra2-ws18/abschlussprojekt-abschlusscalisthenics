package propra2.leihOrDie.dataaccess;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import propra2.leihOrDie.model.Item;

import java.util.List;

public interface ItemRepository extends CrudRepository<Item, Long> {
    List<Item> findAll();

    @Query("SELECT i FROM Item i WHERE i.user.username = :username")
    List<Item> findItemsOfUser(@Param("username") String username);

    @Query("SELECT i FROM Item i WHERE i.id = :id")
    List<Item> findItemById(@Param("id") Long id);
}
