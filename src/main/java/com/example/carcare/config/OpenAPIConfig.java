package com.example.carcare.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI carCareOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Car Care API")
                        .description("API REST para la gestión de vehículos, mantenimientos, repostajes y más")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Juan Carlos Trives")
                                .email("juancarlostrives@hotmail.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Servidor local")
                ));
    }
}
