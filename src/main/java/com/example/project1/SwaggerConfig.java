package com.example.project1;



import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .info(apiInfo())
                .addSecurityItem(new SecurityRequirement().addList("bearer-key"));
    }

    private Info apiInfo() {
        Contact contact = new Contact()
                .name("林韋杉")
                .url("")
                .email("kelly.lin@softmobile.com.tw");

        return new Info()
                .title("圖書商店後台管理系統--API文件")
                .description("這份API文件，包含了所有的API和相關訊息")
                .version("1.0")
               //.termsOfService("http://localhost:8080")
                .contact(contact)
                .license(new License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0"));
    }

    public static final String AUTHORIZATION_HEADER = "Authorization";
}

