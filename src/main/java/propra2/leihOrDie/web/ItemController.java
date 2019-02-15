package propra2.leihOrDie.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import propra2.leihOrDie.dataaccess.ItemRepository;
import propra2.leihOrDie.model.Item;
import propra2.leihOrDie.model.User;

import javax.validation.Valid;

@Controller
public class ItemController {
    @Autowired
    ItemRepository itemRepository;

    @PostMapping("/new")
    public String new_item_post(Model model, @Valid ItemForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "ArtikelEinstellen_only_HTML";
        }

        byte[] pass = {1, 2, 3};
        User dummyUser = new User("dummy", "dummy mail", pass);

        saveItemInRepository(form.getName(), form.getDescription(), form.getCost(), form.getDeposit(), form.getAvailableTime(), form.getLocation(), dummyUser);

        return "redirect:/ArtikelEinstellen_only_HTML";
    }

    @GetMapping("/new")
    public String new_item_get(Model model, @Valid ItemForm form, BindingResult bindingResult) {
        return "ArtikelEinstellen_only_HTML";
    }

    private void saveItemInRepository (String name, String description, int cost, int deposit, int availableTime, String location, User user) {
        Item item = new Item(name, description, cost, deposit, true, availableTime, location, user);
        itemRepository.save(item);
    }

    @GetMapping("/editItem/{id}")
    public String edit_item_get(Model model, @PathVariable Long id, ItemForm form) {
        Item item = itemRepository.findById(id).get();

        form.setName(item.getName());
        form.setDescription(item.getDescription());
        form.setCost(item.getCost());
        form.setDeposit(item.getDeposit());
        form.setAvailability(item.isAvailability());
        form.setAvailableTime(item.getAvailableTime());
        form.setLocation(item.getLocation());

        return "ItemEdit";
    }

    @PostMapping("/editItem/{id}")
    public String edit_item_post(Model model, @PathVariable Long id, @Valid ItemForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "ItemEdit";
        }
         Item item = itemRepository.findById(id).get();
        saveItemInRepository(form.getName(), form.getDescription(), form.getCost(), form.getDeposit(), form.getAvailableTime(), form.getLocation(), form.getUser());
        loadItemIntoForm(model, item);

        return "redirect:/item/{id}";
    }

    private void loadItemIntoForm(Model model, Item item) {
        model.addAttribute("name", item.getName());
        model.addAttribute("description", item.getDescription());
        model.addAttribute("location", item.getLocation());
        model.addAttribute("availableTime", item.getAvailableTime());
        model.addAttribute("deposit", item.getDeposit());
        model.addAttribute("cost", item.getCost());
    }

}
