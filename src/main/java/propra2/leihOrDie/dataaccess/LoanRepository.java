package propra2.leihOrDie.dataaccess;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import propra2.leihOrDie.model.Item;
import propra2.leihOrDie.model.Loan;

import java.util.List;

public interface LoanRepository extends CrudRepository<Loan, Long> {
    List<Loan> findAll();

    @Query("SELECT l FROM Loan l WHERE l.user.username = :username")
    List<Loan> findLoansOfUser(@Param("username") String username);

    @Query("SELECT l FROM Loan l WHERE l.item.id = :itemId")
    List<Loan> findLoansOfItem(@Param("itemId") Long itemId);

    @Query("SELECT l FROM Loan l, Item i WHERE l.item.id = :itemId AND l.status = 'pending' AND i.availability = false AND i.id = :itemId")
    List<Loan> findPendingLoans(@Param("itemId") Long itemId);
}
