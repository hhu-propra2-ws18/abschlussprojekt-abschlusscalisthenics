package propra2.leihOrDie.dataaccess;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import propra2.leihOrDie.model.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, String> {
    List<User> findAll();

    @Query("SELECT u FROM User u WHERE u.email = :email")
    List<User> findUserByEMail(@Param("email") String email);
}
