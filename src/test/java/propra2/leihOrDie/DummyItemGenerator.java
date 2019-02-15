package propra2.leihOrDie;

import propra2.leihOrDie.model.Address;
import propra2.leihOrDie.model.Item;
import propra2.leihOrDie.model.User;

public class DummyItemGenerator {
    public DummyItemGenerator() { }

    public Item generateItem(User user) {
        String name= "Fahrrad";
        String description = "Stereo Hybrid 160 Action Team 500Wh (2018)";
        int cost = 50;
        int deposit= 100;
        boolean availability = true;
        int availableTime = 10;
        String location = user.getAddress().getCity();

        Item item = new Item(name, description, cost,deposit, availability, availableTime, location, user);
        return item;
    }

    public Item generateAnotherItem(User user) {
        String name= "Kickbike";
        String description = "Kickbike Cross MAX 29er – Offroad. Farbe: Schwartz";
        int cost = 80;
        int deposit= 120;
        boolean availability = true;
        int availableTime = 14;
        String location = user.getAddress().getCity();

        Item item = new Item(name, description, cost,deposit, availability, availableTime, location, user);
        return item;
    }
}
