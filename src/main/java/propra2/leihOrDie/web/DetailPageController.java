package propra2.leihOrDie.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import propra2.leihOrDie.dataaccess.PictureRepository;
import propra2.leihOrDie.model.Picture;

import java.util.*;

@Controller
public class DetailPageController {

    /*private static String UPLOADFOLDER = "images/";

    @Autowired
    private PictureRepository pictureRepository;


    @RequestMapping(value ="/image/retrieve", method=RequestMethod.GET)
    public String retrieveAllImages(@RequestParam("itemId") String itemIdString,
                                    RedirectAttributes redirectAttributes) {
        Long itemId = Long.parseLong(itemIdString);

        List<Picture> pictureList = pictureRepository.findPicturesOfItem(itemId);

        List<String> urlList = buildUrls(pictureList);

        return "dummy";
    }

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
    }*/
}
