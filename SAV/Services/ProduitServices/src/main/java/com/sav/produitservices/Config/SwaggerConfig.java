package com.sav.produitservices.Config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "nizar",
                        email = "jawadinizar2@gmail.com",
                        url = "https://nizar.com/dev"
                ),
                description = "OpenApi documentation for spring",
                title = "openApi - SAV ",
                version = "2.0",
                license = @License(
                        name = "Licence name",
                        url = "https://any-url-licence.com"
                ),
                termsOfService = "Termes of services"
        ),
        servers = {
                @Server(
                        description = "Local_ENV",
                        url = "http://localhost:8080"

                ),

        }
)
public class SwaggerConfig {



}