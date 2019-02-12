package propra2.leihOrDie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class leihOrDieApplication {
    public static void main(String[] args) {
        SpringApplication.run(leihOrDieApplication.class, args);
    }

}