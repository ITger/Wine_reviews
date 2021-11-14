package pl.itger.wine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Wine_data_application {

    public static void main(String[] args) {
        SpringApplication.run(Wine_data_application.class, args);
    }

}
