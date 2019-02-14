package propra2.leihOrDie.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Embeddable
public class Address {
    private int postcode;
    private String street;
    private int houseNumber;
    private String city;

    public Address() {}

    public Address(int postcode, String street, int houseNumber, String city) {
        this.postcode = postcode;
        this.street = street;
        this.houseNumber = houseNumber;
        this.city = city;
    }
}
