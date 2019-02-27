package propra2.leihOrDie.dataaccess;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import propra2.leihOrDie.model.Buy;

import java.util.List;

public interface ExceededLoansRepository <ExceededLoans, Long>{
    List<ExceededLoans> findAll();

    @Query("SELECT el FROM ExceededLoans el WHERE el.item.id = :itemId")
    List<Buy> findExceededLoansOfItem(@Param("itemId") Long itemId);

    @Query("SELECT el FROM ExceededLoans el WHERE el.borrower.username = :username")
    List<Buy> findExceededLoansOfUser(@Param("username") String username);
}
