package propra2.leihOrDie;

import propra2.leihOrDie.model.Item;
import propra2.leihOrDie.model.User;

public class DummyItemGenerator {

    public Item generatItem() {
        String name= "Fahrrad";
        String description = "Stereo Hybrid 160 Action Team 500Wh (2018)";
        int cost = 50;
        int deposit= 100;
        boolean availability = true;
        int availableTime = 10;
        String location = "Düsseldorf";
        
        DummyUserGenerator dummyUserGenerator = new DummyUserGenerator();
        User user = dummyUserGenerator.generateUser();

        Item item = new Item(name, description, cost,deposit, availability, availableTime, location, user);
        return item;
    }
}
