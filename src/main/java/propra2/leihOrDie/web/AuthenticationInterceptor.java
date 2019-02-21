package propra2.leihOrDie.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import propra2.leihOrDie.dataaccess.SessionRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private SessionRepository sessionRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
             for(Cookie cookie: cookies) {
                 if(cookie.getName().equals("SessionID")) {
                     if(checkSessionCookie(cookie.getValue())) {
                         return true;
                     }
                 }
             }
        }
        response.sendRedirect("login");
        return false;
    }

    private boolean checkSessionCookie(String sessionId) {
        Optional optional = sessionRepository.findById(sessionId);
        return optional.isPresent();
    }
}
