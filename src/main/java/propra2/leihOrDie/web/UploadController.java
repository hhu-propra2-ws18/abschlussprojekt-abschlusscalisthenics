package propra2.leihOrDie.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import propra2.leihOrDie.dataaccess.ItemRepository;
import propra2.leihOrDie.dataaccess.PictureRepository;
import propra2.leihOrDie.model.Picture;
import propra2.leihOrDie.model.Item;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class UploadController {

    int MAX_NUMBER_OF_PICTURES = 10;

    private static String UPLOADFOLDER = "images/";

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private PictureRepository pictureRepository;


    /*
    @RequestMapping(value="/item/{itemId}/uploadphoto", method=RequestMethod.POST)
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("fileName") String fileName,
                                   @RequestParam("itemId") String itemIdString,
                                   RedirectAttributes redirectAttributes,
                                   @PathVariable Long itemId,
                                   Model model) {

            //Long itemId = Long.parseLong(itemIdString);
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("message", "Bitte Datei zum Hochladen auswählen.");
                return "redirect:/item/{itemId}/uploadphoto";
            }

            if (checkPictureCount(itemId)) {
                redirectAttributes.addFlashAttribute("message", "Maximale Anzahl an Bildern ist erreicht.");
                return "redirect:/item/{itemId}/uploadphoto";
            }

            Item item = itemRepository.findById(itemId).get();

            Picture picture = new Picture(item);
            pictureRepository.save(picture);
            Long pictureId = picture.getId();

            try {
                Path path = buildPath(pictureId, fileName);
                Files.write(path, file.getBytes());

                redirectAttributes.addFlashAttribute("message", "Datei erfolgreich hochgeladen.");

            } catch (IOException error) {
                error.printStackTrace();
                redirectAttributes.addFlashAttribute("message", "Fehler beim Upload.");

                return "redirect:/item/{itemId}/uploadphoto";
            }

            return "redirect:/item/{itemId}/uploadphoto";
    }
    */

    @RequestMapping(value = "/item/{itemId}/uploadphoto", method = RequestMethod.POST)
    public String importParse(@RequestParam("file") MultipartFile file, @PathVariable Long itemId, RedirectAttributes redirectAttributes, Model model) {
        if (file.getSize() == 0) {
            redirectAttributes.addFlashAttribute("message", "Bitte eine Datei wählen :)");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/item/{itemId}/uploadphoto";
        }

        else if (checkPictureCount(itemId)) {
            redirectAttributes.addFlashAttribute("message", "Maximale Anzahl an Bildern ist erreicht.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/item/{itemId}/uploadphoto";
        }

        else {
            Item item = itemRepository.findById(itemId).get();

            Picture picture = new Picture(item);
            pictureRepository.save(picture);
            Long pictureId = picture.getId();

            try {
                Path path = buildPath(pictureId, file.getOriginalFilename());
                Files.write(path, file.getBytes());
                model.addAttribute("message", "Datei erfolgreich hochgeladen.");

            } catch (IOException error) {
                error.printStackTrace();
                model.addAttribute("message", "Fehler beim Upload.");

                //return "redirect:/item/{itemId}/uploadphoto";
                return "redirect:/";
            }

            return "redirect:/item/{itemId}/uploadphoto";
        }

        //return "redirect:/";
    }

    @RequestMapping(value = "/item/{itemId}/uploadphoto", method = RequestMethod.GET)
    public String importParse(@PathVariable Long itemId, RedirectAttributes redirectAttributes) {
        return "item-upload-photo";
    }

    private Path buildPath(Long pictureId, String fileName) {
        String pictureIdString = pictureId.toString();
        String extensionString = fileName.substring(fileName.indexOf("."));

        return Paths.get(UPLOADFOLDER + "/" + pictureIdString
                + extensionString);
    }

    private boolean checkPictureCount(Long itemId) {
        List<Picture> pictureList = pictureRepository.findPicturesOfItem(itemId);
        return (pictureList.size() > (MAX_NUMBER_OF_PICTURES - 1));
    }

    /*
    @GetMapping(value="/image/uploadSuccessful")
    public String uploadSuccessful() {
        return "uploadStatus";
    }

    @GetMapping(value="/image/uploadFailed")
    public String uploadFailed() {
        return "uploadStatus";
    }
    */
}
