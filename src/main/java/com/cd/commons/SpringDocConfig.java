package com.cd.commons;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "My API",
                version = "v1",
                description = "API Documentation",
                contact = @Contact(
                        name = "prakash",
                        email="prakashnamdev115@gmail.com"
                        ,url="http://localhost:8080"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "http://localhost:8080"
                )
        )
)
public class SpringDocConfig {
}
