package propra2.leihOrDie.dataaccess;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import propra2.leihOrDie.model.Transaction;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
        List<Transaction> findAll();

    @Query("SELECT t FROM Transaction t WHERE t.fromUser.username = :username")
    List<Transaction> findTransactionsFromUser(@Param("username") String username);

    @Query("SELECT t FROM Transaction t WHERE t.toUser.username = :username")
    List<Transaction> findTransactionsToUser(@Param("username") String username);
}
