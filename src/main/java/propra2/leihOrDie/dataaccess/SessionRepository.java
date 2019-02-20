package propra2.leihOrDie.dataaccess;

import org.springframework.data.repository.CrudRepository;
import propra2.leihOrDie.model.Session;

import java.util.List;

public interface SessionRepository extends CrudRepository<Session, String> {
    List<Session> findAll();
}
