package pl.itger.wine;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.util.Collections;

@Configuration
@EnableSwagger2
//@EnableSwagger2WebMvc
public class SwaggerConfig {

    @Bean
    public Docket postsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(
                        PathSelectors
                                .any()
                )
                .build()
                .pathMapping("/")
                .apiInfo(metadata())
                .forCodeGeneration(true)
                .useDefaultResponseMessages(false)
                ;

//        return new Docket(DocumentationType.SWAGGER_2).groupName("wine-resource-api")
//                .apiInfo(metadata())//.select().paths(postPaths()).build();
//                .select()
//                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.any())
//                .build()
//                .pathMapping("/api/WineGlass")
//                .directModelSubstitute(LocalDate.class, String.class)
//                .genericModelSubstitutes(ResponseEntity.class);
    }

    private ApiInfo metadata() {
        ApiInfo apiInfo = new ApiInfo("Wine resource API",
                "Wine resource",
                "API TOS",
                "Terms of service",
                new Contact("Piotr Zerynger", "itger.pl", "p.zerynger@gmail.com"),
                "License of API", "API license URL", Collections.emptyList());
        return apiInfo;
    }

}