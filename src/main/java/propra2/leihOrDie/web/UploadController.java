package propra2.leihOrDie.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    private static String UPLOADFOLDER = "images/";

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private PictureRepository pictureRepository;


    @RequestMapping(value="/image/upload", method=RequestMethod.POST)
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("fileName") String fileName,
                                   @RequestParam("itemId") String itemIdString,
                                   RedirectAttributes redirectAttributes) {

        Long itemId = Long.parseLong(itemIdString);
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Bitte Datei zum Hochladen auswählen.");
            return "redirect:/image/uploadFailed";
        }

        if (checkPictureCount(itemId)) {
            redirectAttributes.addFlashAttribute("message", "Maximale Anzahl an Bildern ist erreicht.");
            return "redirect:/image/uploadFailed";
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

            return "redirect:/image/uploadFailed";
        }

        return "redirect:/image/uploadSuccessful";
    }

    private Path buildPath(Long pictureId, String fileName) {
        String pictureIdString = pictureId.toString();
        String extensionString = fileName.substring(fileName.indexOf("."));

        return Paths.get(UPLOADFOLDER + "/" + pictureIdString
                + extensionString);
    }

    private boolean checkPictureCount(Long itemId) {
        List<Picture> pictureList = pictureRepository.findPicturesOfItem(itemId);

        return pictureList.size() > 10;
    }

    @GetMapping(value="/image/uploadSuccessful")
    public String uploadSuccessful() {
        return "uploadStatus";
    }

    @GetMapping(value="/image/uploadFailed")
    public String uploadFailed() {
        return "uploadStatus";
    }
}
