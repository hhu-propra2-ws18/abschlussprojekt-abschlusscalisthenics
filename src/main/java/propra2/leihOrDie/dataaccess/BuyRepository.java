package propra2.leihOrDie.dataaccess;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import propra2.leihOrDie.model.Buy;
import propra2.leihOrDie.model.Loan;

import java.util.List;

public interface BuyRepository extends CrudRepository<Buy, Long> {
    List<Buy> findAll();

    @Query("SELECT b FROM Buy b WHERE b.item.id = :itemId")
    List<Loan> findBuysOfItem(@Param("itemId") Long itemId);

    @Query("SELECT b FROM Buy b WHERE b.buyer.username = :username")
    List<Loan> findBuysOfUser(@Param("username") String username);
}
