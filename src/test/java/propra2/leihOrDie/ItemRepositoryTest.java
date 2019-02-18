package propra2.leihOrDie;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import propra2.leihOrDie.dataaccess.ItemRepository;
import propra2.leihOrDie.dataaccess.UserRepository;
import propra2.leihOrDie.model.Item;
import propra2.leihOrDie.model.User;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @After
    public void deleteAllTestItems() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testCreateItem() throws Exception {
        String name= "Fahrrad";
        String description = "Stereo Hybrid 160 Action Team 500Wh (2018)";
        int cost = 50;
        int deposit= 100;
        boolean availability = true;
        int availableTime = 10;
        DummyUserGenerator dummyUserGenerator = new DummyUserGenerator();
        User user = dummyUserGenerator.generateUser();
        Item item = new Item(name, description, cost,deposit, availability, availableTime, user);

        Assert.assertEquals(item.getName(), "Fahrrad");
        Assert.assertEquals(item.getDescription(), "Stereo Hybrid 160 Action Team 500Wh (2018)");
        Assert.assertEquals(item.getCost(), 50);
        Assert.assertEquals(item.getDeposit(), 100);
        Assert.assertEquals(item.getAvailableTime(), 10);
    }

    @Test
    public void saveOneItem() throws Exception {
        DummyUserGenerator dummyUserGenerator = new DummyUserGenerator();
        User user = dummyUserGenerator.generateUser();
        DummyItemGenerator dummyItemGenerator = new DummyItemGenerator();
        Item item= dummyItemGenerator.generateItem(user);
        userRepository.save(user);
        itemRepository.save(item);
        List<Item> itemList = itemRepository.findAll();
        Assertions.assertThat(itemList.size()).isEqualTo(1);
        Assertions.assertThat(itemList.get(0).getName()).isEqualTo("Fahrrad");
    }

    @Test
    public void saveSeveralItems() throws Exception {
        DummyUserGenerator dummyUserGenerator = new DummyUserGenerator();
        User user = dummyUserGenerator.generateUser();
        DummyItemGenerator dummyItemGenerator = new DummyItemGenerator();
        Item firstItem= dummyItemGenerator.generateItem(user);
        Item secondItem = dummyItemGenerator.generateAnotherItem(user);
        userRepository.save(user);
        itemRepository.save(firstItem);
        itemRepository.save(secondItem);

        List<Item> itemList = itemRepository.findAll();

        Assertions.assertThat(itemList.size()).isEqualTo(2);
        Assertions.assertThat(itemList.get(1).getName()).isEqualTo("Fahrrad");
        Assertions.assertThat(itemList.get(0).getName()).isEqualTo("Kickbike");

        Item firstItemFromRepo = itemList.get(1);
        Assertions.assertThat(firstItemFromRepo.getName()).isEqualTo("Fahrrad");
        Assertions.assertThat(firstItemFromRepo.getDescription()).isEqualTo("Stereo Hybrid 160 Action Team 500Wh (2018)");
        Assertions.assertThat(firstItemFromRepo.getCost()).isEqualTo(50);
        Assertions.assertThat(firstItemFromRepo.getDeposit()).isEqualTo(100);
        Assertions.assertThat(firstItemFromRepo.getAvailableTime()).isEqualTo(10);

        Item secondItemFromRepo = itemList.get(0);
        Assertions.assertThat(secondItemFromRepo.getName()).isEqualTo("Kickbike");
        Assertions.assertThat(secondItemFromRepo.getDescription()).isEqualTo("Kickbike Cross MAX 29er – Offroad. Farbe: Schwartz");
        Assertions.assertThat(secondItemFromRepo.getCost()).isEqualTo(80);
        Assertions.assertThat(secondItemFromRepo.getDeposit()).isEqualTo(120);
        Assertions.assertThat(secondItemFromRepo.getAvailableTime()).isEqualTo(14);
    }

    @Test
    public void editItem() throws Exception {
        DummyUserGenerator dummyUserGenerator = new DummyUserGenerator();
        User user = dummyUserGenerator.generateUser();
        DummyItemGenerator dummyItemGenerator = new DummyItemGenerator();
        Item item= dummyItemGenerator.generateItem(user);
        userRepository.save(user);
        itemRepository.save(item);

        List<Item> itemList = itemRepository.findAll();
        Item itemFromRepo = itemList.get(0);

        itemFromRepo.setName("Schwadmäher");
        itemRepository.save(itemList.get(0));
        itemList = itemRepository.findAll();
        Assertions.assertThat(itemList.get(0).getName()).isEqualTo("Schwadmäher");

        itemFromRepo = itemList.get(0);
        itemFromRepo.setDescription("E318 f. E303 Schwadmäher");
        itemRepository.save(itemList.get(0));
        itemList = itemRepository.findAll();
        Assertions.assertThat(itemList.get(0).getDescription()).isEqualTo("E318 f. E303 Schwadmäher");

        itemFromRepo = itemList.get(0);
        itemFromRepo.setCost(850);
        itemRepository.save(itemList.get(0));
        itemList = itemRepository.findAll();
        Assertions.assertThat(itemList.get(0).getCost()).isEqualTo(850);

        itemFromRepo = itemList.get(0);
        itemFromRepo.setDeposit(1000);
        itemRepository.save(itemList.get(0));
        itemList = itemRepository.findAll();
        Assertions.assertThat(itemList.get(0).getDeposit()).isEqualTo(1000);
    }

    @Test
    public void allItemsOfUser() throws Exception {
        DummyUserGenerator dummyUserGenerator = new DummyUserGenerator();
        User firstUser = dummyUserGenerator.generateUser();
        User secondUser = dummyUserGenerator.generateUser();
        DummyItemGenerator dummyItemGenerator = new DummyItemGenerator();
        Item firstItem= dummyItemGenerator.generateItem(firstUser);
        Item secondItem = dummyItemGenerator.generateAnotherItem(firstUser);
        Item thirdItem= dummyItemGenerator.generateItem(secondUser);
        userRepository.save(firstUser);
        userRepository.save(secondUser);
        itemRepository.save(firstItem);
        itemRepository.save(secondItem);
        itemRepository.save(thirdItem);

        List<Item> itemOfFirstUser = itemRepository.findItemsOfUser(firstUser.getUsername());
        Assertions.assertThat(itemOfFirstUser.size()).isEqualTo(2);
        Assertions.assertThat(itemOfFirstUser.get(0).getName()).isEqualTo("Fahrrad");
        Assertions.assertThat(itemOfFirstUser.get(1).getName()).isEqualTo("Kickbike");
        List<Item> itemOfSecondUser = itemRepository.findItemsOfUser(secondUser.getUsername());
        Assertions.assertThat(itemOfSecondUser.size()).isEqualTo(1);
        Assertions.assertThat(itemOfSecondUser.get(0).getName()).isEqualTo("Fahrrad");
    }
}
