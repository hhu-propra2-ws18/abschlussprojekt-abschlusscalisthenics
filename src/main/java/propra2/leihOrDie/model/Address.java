package propra2.leihOrDie.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Address {
    @Id
    @Column(name="address_id")
    private Long id;

    private int postcode;
    private String street;
    private String houseNumber;
    private String district;

    public Address() {}

    public Address(int postcode, String street, String houseNumber, String district) {
        this.postcode = postcode;
        this.street = street;
        this.houseNumber = houseNumber;
        this.district = district;
    }
}
