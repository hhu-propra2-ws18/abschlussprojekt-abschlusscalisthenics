package propra2.leihOrDie.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import propra2.leihOrDie.dataaccess.ItemRepository;
import propra2.leihOrDie.dataaccess.PictureRepository;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import propra2.leihOrDie.dataaccess.SessionRepository;
import propra2.leihOrDie.dataaccess.UserRepository;
import propra2.leihOrDie.model.Item;
import propra2.leihOrDie.model.Picture;
import propra2.leihOrDie.model.User;


import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ItemController {

    private static String UPLOADFOLDER = "images/";

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PictureRepository pictureRepository;
    @Autowired
    SessionRepository sessionRepository;

    @PostMapping("/item/create")
    public String newItem(Model model, @Valid ItemForm form, BindingResult bindingResult, @CookieValue(value="SessionID", defaultValue="") String sessionId) {
        User user = sessionRepository.findUserBySessionCookie(sessionId);

        if (bindingResult.hasErrors()) {
            return "create-item";
        }

        Item item = new Item(form.getName(), form.getDescription(), form.getCost(), form.getDeposit(), true,
                form.getAvailableTime(), user.getAddress(), user);
        saveItem(item);

        return "redirect:/item/" + item.getId() + "/uploadphoto";
        //return "redirect:/borrowall";
    }

    @GetMapping("/item/create")
    public String newItem(Model model, ItemForm form) {
        return "create-item";
    }


    @GetMapping("/item/edit/{id}")
    public String editItem(Model model, @PathVariable Long id, ItemForm form) {
        Item item = itemRepository.findById(id).get();

        form.setName(item.getName());
        form.setDescription(item.getDescription());
        form.setCost(item.getCost());
        form.setDeposit(item.getDeposit());
        form.setAvailability(item.isAvailability());
        form.setAvailableTime(item.getAvailableTime());

        return "edit-item";
    }

    @PostMapping("/item/edit/{id}")
    public String editItem(Model model, @PathVariable Long id, @Valid ItemForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit-item";
        }
        Item item = itemRepository.findById(id).get();
        loadItemIntoForm(model, item);
        saveItem(item);

        return "redirect:/";
    }

    @GetMapping("/borrowall/{id}")
    public String showItem(Model model, @PathVariable Long id) {
        Item item = itemRepository.findById(id).get();
        model.addAttribute("LoanForm", new LoanForm());

        loadItemIntoForm(model, item);

        List<Picture> pictureList = pictureRepository.findPicturesOfItem(id);


        List<String> urlList = buildUrls(pictureList);
        //List<String> urlList = new ArrayList<>();
        urlList.add("https://images.pexels.com/photos/730896/pexels-photo-730896.jpeg?cs=srgb&dl=adorable-animal-cat-730896.jpg");
        urlList.add("https://images.pexels.com/photos/96938/pexels-photo-96938.jpeg?cs=srgb&dl=animal-animal-photography-cat-96938.jpg");
        model.addAttribute("pictures", urlList);
        model.addAttribute("numOfPictures", urlList.size());

        return "item-detail.html";
    }

    @GetMapping("/borrowall")
    public String listAllItems(Model model) {
        // User is missing has to be added
        model.addAttribute("items", itemRepository.findAll());
        return "item-list";
    }

    /*
    @RequestMapping(value ="/borrowall/{id}", method=RequestMethod.GET)
    public String retrieveAllImages(@RequestParam("itemId") String itemIdString,
                                    RedirectAttributes redirectAttributes) {
        Long itemId = Long.parseLong(itemIdString);

        List<Picture> pictureList = pictureRepository.findPicturesOfItem(itemId);

        List<String> urlList = buildUrls(pictureList);

        return "dummy";
    }
    */

    private List<String> buildUrls(List<Picture> pictureList) {
        List<String> urlList = new ArrayList<>();

        if(pictureList.size() == 0) {
            return urlList;
        }

        for(Picture picture: pictureList) {
            urlList.add(buildUrl(picture));
        }

        return urlList;
    }

    private String buildUrl(Picture picture) {
        String raw = "/images/";
        String idString = picture.getId().toString();

        return raw + idString;
    }

    private void loadItemIntoForm(Model model, Item item) {
        model.addAttribute("name", item.getName());
        model.addAttribute("description", item.getDescription());
        model.addAttribute("location", item.getUser().getAddress().getCity());
        model.addAttribute("availableTime", item.getAvailableTime());
        model.addAttribute("deposit", item.getDeposit());
        model.addAttribute("cost", item.getCost());
        model.addAttribute("username", item.getUser().getUsername());
        model.addAttribute("isAvailable", item.isAvailability());
    }

    private void saveItem(Item item) {
        itemRepository.save(item);
    }
}
