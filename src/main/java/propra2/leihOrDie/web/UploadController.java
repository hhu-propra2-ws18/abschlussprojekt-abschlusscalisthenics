package propra2.leihOrDie.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import propra2.leihOrDie.dataaccess.ItemRepository;

import java.io.IOException;

@Controller
public class UploadController {

    private static String UPLOADFOLDER = "images/";
    private ItemRepository itemRepository;

    @GetMapping("/")
    public String index() {
        return "upload";
    }

    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file")MultipartFile file,
                                   @RequestParam("itemId") Long itemId,
                                   RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Bitte Datei zum Hochladen auswählen");
            return "redirect:uploadStatus";
        }

        String imageId = itemRepository.findById(itemId)

        try {
            byte[] bytes = file.getBytes();


        } catch (IOException error) {
            error.printStackTrace();
        }

        return "";
    }
}
