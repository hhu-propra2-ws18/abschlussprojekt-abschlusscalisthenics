package propra2.leihOrDie.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorController {

    public String getErrorCode(HttpServletRequest request){
        String statusCode = (String)request.getAttribute("javax.servlet.error.status_code");
        //Exception exception = (Exception)request.getAttribute("javax.servlet.error.exception");
        return statusCode;
    }

    @RequestMapping("/error")
    public ModelAndView renderErrorpage(HttpServletRequest request){
        ModelAndView errorPage = new ModelAndView("errorPage");
        String statusCode = getErrorCode(request);
        String errorMessage = "";
        switch(statusCode){
            case "400":{
                errorMessage = "400. Falsche Anfrage";
                break;
            }
            case "401":{
                errorMessage = "401. Nicht autorisiert";
                break;
            }
            case "404":{
                errorMessage = "404. Quelle nicht gefunden";
                break;
            }
            case "500":{
                errorMessage = "500. Serverfehler";
                break;
            }
        }
        errorPage.addObject("errorMessage", errorMessage);
        return errorPage;
    }
}