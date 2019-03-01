package propra2.leihOrDie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import propra2.leihOrDie.security.AdminInterceptor;
import propra2.leihOrDie.security.AuthenticationInterceptor;
import propra2.leihOrDie.security.SignOutButtonInterceptor;

import java.util.Arrays;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {
    @Bean
    AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }

    @Bean
    SignOutButtonInterceptor signOutButtonInterceptor() {
        return new SignOutButtonInterceptor();
    }

    @Bean
    AdminInterceptor adminInterceptor() {
        return new AdminInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor())
                .excludePathPatterns(Arrays.asList("/", "/borrowall/**", "/signout", "/images/**", "/help"));

        registry.addInterceptor(adminInterceptor())
                .addPathPatterns("/");

        registry.addInterceptor(signOutButtonInterceptor())
                .excludePathPatterns("/signout");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:./img/");
    }
}
