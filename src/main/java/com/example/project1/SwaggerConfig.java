package com.example.project1;


//import io.swagger.models.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
//import io.swagger.models.License;
import io.swagger.v3.oas.models.info.License;
//import io.swagger.models.SecurityRequirement;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityScheme;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
//import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
/*import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.service.Contact;*/

import java.util.ArrayList;


@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .info(apiInfo());
    }

    private Info apiInfo() {
        Contact contact = new Contact()
                .name("林韋杉")
                .url("http://your-url.com") // 可以填寫你的網址，或留空字串
                .email("kelly.lin@softmobile.com.tw");

        return new Info()
                .title("API 文檔")
                .description("這是書店後台管理系統的 API 文檔，包含了所有的 API 端點和相關訊息")
                .version("1.0")
                .termsOfService("http://localhost:8080")
                .contact(contact)
                .license(new License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0"));
    }


    /*public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.*"))
                .paths(PathSelectors.any())
                .build();
    }*/
    public static final String AUTHORIZATION_HEADER = "Authorization";

   // Contact contact = new Contact("xx","xxx","xxxxxx");

    /*private ApiKey apiKey(){
        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
    }*/


    /*public ApiInfo apiInfo(){
        return new ApiInfo(
                "Api文檔",
                "備註",
                "1.0",
                "123",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<>());
    }*/




   /* @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("Public API")
                .packagesToScan("com.example.project1.controller")
                .build();
    }*/


}

