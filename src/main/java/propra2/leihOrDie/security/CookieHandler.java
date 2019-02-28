package propra2.leihOrDie.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import propra2.leihOrDie.dataaccess.SessionRepository;

import javax.servlet.http.Cookie;
import java.util.Optional;

@Service
public class CookieHandler {
    @Autowired
    private SessionRepository sessionRepository;

    private boolean checkSessionCookie(String sessionId) {
        Optional optional = sessionRepository.findById(sessionId);
        return optional.isPresent();
    }

    public boolean checkIfLoggedIn(Cookie cookies[]) {
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("SessionID") && checkSessionCookie(cookie.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }
}
