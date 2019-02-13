package propra2.leihOrDie.dataaccess;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import propra2.leihOrDie.model.Item;
import propra2.leihOrDie.model.User;

import java.util.List;

public interface ItemRepository extends CrudRepository<Item, Long> {
    List<Item> findAll();

    @Query("SELECT i FROM Item i WHERE i.user.username = :username")
    List<Item> findItemsFromUser(@Param("username") String username);

}
