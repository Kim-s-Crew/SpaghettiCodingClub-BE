package wercsmik.spaghetticodingclub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpaghettiCodingClubApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpaghettiCodingClubApplication.class, args);
    }

}
