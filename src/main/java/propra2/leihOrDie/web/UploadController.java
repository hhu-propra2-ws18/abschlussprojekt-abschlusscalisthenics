package propra2.leihOrDie.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/")
    public String index() {
        return "upload";
    }

    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file")MultipartFile file,
                                   @RequestParam("itemId") Long itemId,
                                   RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Bitte Datei zum Hochladen auswählen.");
            return "redirect:uploadStatus";
        }

        if (checkPictureCount(itemId)) {
            redirectAttributes.addFlashAttribute("message", "Maximale Anzahl an Bildern ist erreicht.");
            return "redirect:uploadStatus";
        }

        Item item = itemRepository.findById(itemId).get();

        Picture picture = new Picture(item);
        Long pictureId = picture.getId();
        String fileName = file.getName();

        try {
            Path path = buildPath(pictureId, itemId, fileName);
            write(path, file);

            redirectAttributes.addFlashAttribute("message", "Datei erfolgreich hochgeladen.");

        } catch (IOException error) {
            error.printStackTrace();
        }

        return "redirect:/uploadStatus";
    }

    private Path buildPath(Long pictureId, Long itemId, String fileName) {
        String itemIdString = itemId.toString();
        String pictureIdString = pictureId.toString();
        String extensionString = fileName.substring(fileName.indexOf("."));

        return Paths.get(UPLOADFOLDER + "/" + itemIdString + "/" + pictureIdString +
                "." + extensionString);
    }

    private void write(Path path, MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();

        Files.write(path, bytes);
    }

    private boolean checkPictureCount(Long itemId) {
        List<Picture> pictureList = pictureRepository.findPicturesOfItem(itemId);
        if (pictureList.size() > 10) {
            return false;
        } else {
            return false;
        }
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }
}
