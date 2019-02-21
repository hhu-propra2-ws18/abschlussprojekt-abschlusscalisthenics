package propra2.leihOrDie.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SignOutButtonInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private CookieHandler cookieHandler;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        boolean loggedIn = (cookieHandler.checkIfLoggedIn(request.getCookies()));
        modelAndView.addObject("isLoggedIn", loggedIn);

    }
}
