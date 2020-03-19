package pl.itger.wine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
//@ComponentScan(basePackages = {"pl.itger.dataFaker", "pl.itger.JWTokens", "pl.itger.PolishAPI"})
//@EnableSwagger2
public class Wine_data_application {

//    @Autowired
//    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(Wine_data_application.class, args);
    }


}
