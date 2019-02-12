package propra2.leihOrDie.dataaccess;

import org.springframework.data.repository.CrudRepository;
import propra2.leihOrDie.model.Loan;

import java.util.List;

public interface LoanRepository extends CrudRepository<Loan, Long> {
    List<Loan> findAll();
}
