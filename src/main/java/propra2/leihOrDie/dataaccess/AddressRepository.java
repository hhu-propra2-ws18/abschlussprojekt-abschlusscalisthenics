package propra2.leihOrDie.dataaccess;

import org.springframework.data.repository.CrudRepository;
import propra2.leihOrDie.model.Address;

import java.util.List;

public interface AddressRepository extends CrudRepository<Address, Long> {
    List<Address> findAll();

}
